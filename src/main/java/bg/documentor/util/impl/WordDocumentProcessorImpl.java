package bg.documentor.util.impl;

import bg.documentor.enums.SubType;
import bg.documentor.enums.Type;
import bg.documentor.enums.Validator;
import bg.documentor.model.*;
import bg.documentor.repository.FormTemplateRepository;
import bg.documentor.service.FormTemplateService;
import bg.documentor.util.WordDocumentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.Docx4J;
import org.docx4j.TraversalUtil;
import org.docx4j.TraversalUtil.Callback;
import org.docx4j.XmlUtils;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j @Component public class WordDocumentProcessorImpl implements WordDocumentProcessor {

	private final IntelliCrawler intelliCrawler;
	private final FormTemplateService formTemplateService;
	private final FormTemplateRepository formTemplateRepository;
	/**
	 * regex pattern identifying FieldName, comma-separated Validations and comma-separated Options
	 * pattern group 1 - FieldName
	 * pattern group 5 - comma-separated Validations
	 * pattern group 9 - comma-separated Options
	 */
	private final String pattern_AllIn = "\\$\\{\\s*([a-zA-Zа-яА-Я0-9]+)\\s*((\\|){1}\\s*(([a-zA-Zа-яА-Я0-9,\\s]+)\\s*)?)?((\\$){1}\\s*(([a-zA-Zа-яА-Я0-9,\\s]+)\\s*)?)?}";
	@Value("${fileWatcher.processDir}") private File processDir;
	@Value("${fileWatcher.genDir}") private File genDir;

	public WordDocumentProcessorImpl(FormTemplateService formTemplateService,
			FormTemplateRepository formTemplateRepository, IntelliCrawler intelliCrawler) {
		this.formTemplateService = formTemplateService;
		this.formTemplateRepository = formTemplateRepository;
		this.intelliCrawler = intelliCrawler;
	}

	/**
	 * Load XML part of a MSWord doc file
	 *
	 * @param fileToParse
	 * @return
	 */
	@Override public WordprocessingMLPackage getWordProcessingMLPackage(File fileToParse) {
		WordprocessingMLPackage wordMLPackage = new WordprocessingMLPackage();

		try {
			wordMLPackage = Docx4J.load(fileToParse);

		} catch (Docx4JException e) {
			log.error("Error while parsing file " + fileToParse.getName(), e);
		}
		return wordMLPackage;
	}

	@Override public void parseWordDocGamma(File fileToParse) {
		List<FormField> formFieldList = new ArrayList<>();
		FormTemplate formTemplate = FormTemplate.builder()
				.value(fileToParse.getName()/*.substring(0,fileToParse.getName().lastIndexOf("."))*/).build();

		WordprocessingMLPackage wordMLPackage = getWordProcessingMLPackage(fileToParse);

		MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
		try {
			VariablePrepare.prepare(wordMLPackage);
		} catch (Exception e) {
			log.error("Error while parsing file " + fileToParse.getName(), e);
		}

		List<String> tagValues = getTagValues(mainDocumentPart.getXML());

		String[] validations = null;
		String[] options = null;

		int i = 0;
		for (String tagValue : tagValues) {
			String mainValue = patternMatcher(tagValue, pattern_AllIn, 1);
			String validationsValue = patternMatcher(tagValue, pattern_AllIn, 5);
			String optionsValue = patternMatcher(tagValue, pattern_AllIn, 9);

			i += 1;
			FormField formField = FormField.builder().build();
			formField.setType(Type.input);
			formField.setSubType(SubType.text);
			formField.setKey(tagValue);
			formField.setLabel(mainValue);
			/**
			 * set validators if any
			 */
			if (validationsValue != null) {
				List<FieldValidator> fieldValidatorList = new ArrayList<>();
				validations = validationsValue.trim().split(",");
				for (int y = 0; y < validations.length; y++) {
					FieldValidator validator = new FieldValidator();
					validator.setKey(Validator.valueOf(validations[y]));
					validator.setFormField(formField);
					fieldValidatorList.add(validator);
				}
				formField.setValidators(fieldValidatorList);
			}
			/**
			 * set options if any
			 */
			if (optionsValue != null) {
				List<FieldOption> fieldOptionList = new ArrayList<>();
				options = optionsValue.trim().split(",");
				for (int z = 0; z < options.length; z++) {
					FieldOption option = FieldOption.builder().key(Integer.toString(z)).value(options[z])
							.formField(formField).build();
					fieldOptionList.add(option);
				}
				formField.setOptions(fieldOptionList);
			}

			formField.setOrder(i);
			formField.setFormTemplate(formTemplate);
			formFieldList.add(formField);
			//			mappings2.put(mainValue,innerHashMap);

			/**
			 *  assign a value to the placeholder
			 */
			//			mappings.put(tagValue.substring(2,tagValue.length()-1), "anyValue");

		}
		formTemplate.setFormFields(formFieldList);
		formTemplateRepository.save(formTemplate);

	}

	@Override public void parseWordDocTeta(File fileToParse) {
		FormTemplate formTemplate = FormTemplate.builder().value(fileToParse.getName()).build();

		WordprocessingMLPackage wordMLPackage = getWordProcessingMLPackage(fileToParse);

		MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
		// NEW BEGINNING
		final int[] y = { 0 };
		final BigInteger[] bi = new BigInteger[1];
		final String[] biString = { null };

		List<FormField> formFieldList = new ArrayList<>();
		intelliCrawler.mainCrawl(mainDocumentPart).entrySet().forEach(nestedMap -> {
			Map<String, Object> nestedObject = nestedMap.getValue();
			y[0] += 1;
			FormField formField = FormField.builder().build();

			formField.setType(Type.input);
			formField.setSubType(SubType.text);

			bi[0] = nestedMap.getKey();
			biString[0] = bi[0].toString();
			formField.setKey(biString[0]);
			SdtPr.Alias alias = ((SdtPr.Alias) nestedObject.get("Alias"));
			if (alias != null) {
				formField.setLabel(alias.getVal());
			} else {
				formField.setLabel("No label");
			}
			/**
			 * get DropDownList if any
			 */
			CTSdtDropDownList ctSdtDropDownList = (CTSdtDropDownList) nestedObject.get("CTSdtDropDownList");
			if (ctSdtDropDownList != null) {

				formField.setType(Type.select);
				formField.setSubType(SubType.single);

				List<CTSdtListItem> sdtListItem = ctSdtDropDownList.getListItem();

				List<FieldOption> fieldOptionList = new ArrayList<>();
				for (CTSdtListItem lCombo : sdtListItem) {
					//					lString.add(lCombo.getDisplayText());
					FieldOption fieldOption = FieldOption.builder().key(lCombo.getDisplayText())
							.value(lCombo.getValue()).formField(formField).build();
					fieldOptionList.add(fieldOption);
				}
				formField.setOptions(fieldOptionList);
			}
			/**
			 * get a Date if any
			 */
			CTSdtDate ctSdtDate = (CTSdtDate) nestedObject.get("CTSdtDate");
			if (ctSdtDate != null) {

				formField.setType(Type.input);
				formField.setSubType(SubType.date);
			}

			formField.setOrder(y[0]);
			formField.setFormTemplate(formTemplate);
			formFieldList.add(formField);
		});

		formTemplate.setFormFields(formFieldList);

		formTemplateRepository.save(formTemplate);

	}

	@Override public void generateMSwordFile(List<FormFieldHist> formFieldHistList) {
		String fileName = formFieldHistList.get(0).getFormTemplate().getValue();
		File fileToParse = new File(processDir + "\\" + fileName);
		WordprocessingMLPackage wordMLPackage = getWordProcessingMLPackage(fileToParse);
		MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
		try {
			VariablePrepare.prepare(wordMLPackage);
		} catch (Exception e) {
			log.error("Error while parsing file " + fileToParse.getName(), e);
		}

		Map<String, String> mappings = new HashMap<>();
		//			mappings.put(tagValue.substring(2,tagValue.length()-1), "anyValue");
		formFieldHistList.forEach(formFieldHist -> {
			String tagKey = patternMatcher(formFieldHist.getFormField().getKey(), "\\$\\{(.*?)\\}", 1);
			String tagValue = formFieldHist.getValue();
			mappings.put(tagKey, tagValue);
		});
		try {
			mainDocumentPart.variableReplace(mappings);
		} catch (Docx4JException | JAXBException e) {
			log.error("Error while variableReplace ", e);
		}
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		String pathGenFile = genDir + "\\" + /*tokens[0] +*/ "TESTFILE_" + "_userId_01_" + /*new Date() +*/ "." + tokens[1];
		File genFile = new File(pathGenFile);
		try {
			wordMLPackage.save(genFile);
		} catch (Docx4JException e) {
			log.error("Error while saving new file " + fileToParse.getName(), e);
		}

	}

	@Override public void generateWordFile(List<FormFieldHist> formFieldHistList) {
		String fileName = formFieldHistList.get(0).getFormTemplate().getValue();
		File fileToParse = new File(processDir + "\\" + fileName);
		WordprocessingMLPackage wordMLPackage = getWordProcessingMLPackage(fileToParse);
		MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();

		//		List<HashMap<String, Object>>  listSdtItems = intelliCrawler.mainCrawl(mainDocumentPart);
		Map<BigInteger, Map<String, Object>> mapSdtItems = intelliCrawler.mainCrawl(mainDocumentPart);
		/**
		 * create two necessary iterators
		 */
		//		Iterator<HashMap<String, Object>> fileFieldsIter = listSdtItems.iterator();
		for (FormFieldHist fieldHist : formFieldHistList) {
			String key = fieldHist.getFormField().getKey();
			BigInteger bIkey = new BigInteger(key);
			Map<String, Object> hashMap = mapSdtItems.get(bIkey);
			Object textObject = hashMap.get("Text");
			((Text) textObject).setValue(fieldHist.getValue());
		}

		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		String pathGenFile = genDir + "\\" + /*tokens[0] +*/ "TESTFILE_" + "_userId_01_" + /*new Date() +*/ "." + tokens[1];
		File genFile = new File(pathGenFile);
		try {
			wordMLPackage.save(genFile);
		} catch (Docx4JException e) {
			log.error("Error while saving new file " + fileToParse.getName(), e);
		}

	}

	@Override public void parseWordDocBeta(File fileToParse) {

		List<FormField> formFieldList = new ArrayList<>();
		FormTemplate formTemplate = FormTemplate.builder().value(fileToParse.getName()).build();

		WordprocessingMLPackage wordMLPackage = null;

		try {
			wordMLPackage = WordprocessingMLPackage.load(fileToParse);
		} catch (Docx4JException e) {
			log.error("Error while parsing " + fileToParse.getName(), e);
		}

		MainDocumentPart mainDocumentPart = null;

		if (wordMLPackage != null) {
			mainDocumentPart = wordMLPackage.getMainDocumentPart();

			//get elements
			org.docx4j.wml.Document wmlDocumentEl = mainDocumentPart.getJaxbElement();
			Body body = wmlDocumentEl.getBody();
			// https://github.com/plutext/docx4j/blob/master/docx4j-samples-docx4j/src/main/java/org/docx4j/samples/OpenMainDocumentAndTraverse.java
			new TraversalUtil(body,

					new Callback() {

						String indent = "";

						@Override public List<Object> apply(Object o) {

							String wrapped = "";
							if (o instanceof JAXBElement)
								wrapped = " (wrapped in JAXBElement)";

							o = XmlUtils.unwrap(o);

							String text = "";
							if (o instanceof org.docx4j.wml.Text)
								text = ((org.docx4j.wml.Text) o).getValue();

							if ((o instanceof org.docx4j.wml.SdtBlock))
							//								||
							//								(o instanceof org.docx4j.wml.SdtRun)||
							//								(o instanceof org.docx4j.wml.CTSdtRow)||
							//								(o instanceof org.docx4j.wml. CTSdtCell))
							{
								log.info("EURIKA! BEGINS with SdtBlock_ID: " + ((SdtBlock) o).getSdtPr().getId());

								if (((SdtBlock) o).getSdtPr().getTag() != null) {
									SdtPr sdtPr = ((org.docx4j.wml.SdtElement) o).getSdtPr();
									//									SdtPr sdtPr = ((org.docx4j.wml.SdtElement) o).getSdtContent().getContent();
									List<Object> sdtPrContent = /*sdtPr.getRPrOrAliasOrLock()*/ ((org.docx4j.wml.SdtElement) o)
											.getSdtContent().getContent();

									FormField formField = FormField.builder().build();

									for (int i = 0; i < sdtPrContent.size(); i++) {
										if (sdtPrContent.get(i).getClass().getName()
												.equals("javax.xml.bind.JAXBElement")) {
											Object o2 = XmlUtils.unwrap(sdtPrContent.get(i));
											log.info("order no: " + i + " unwrapped " + o2.getClass().getName());
											processSubClass(o2, formField);
										} else {
											log.info("order no: " + i + " " + sdtPrContent.get(i).getClass().getName());
											processSubClass(sdtPrContent.get(i), formField);
										}
										log.info("EURIKA! ENDS");
									}
									formFieldList.add(formField);
									formTemplate.setFormFields(formFieldList);
									formTemplateService.save(formTemplate);

									log.info("Check the DTO!");
								}
							}

							log.info(indent + o.getClass().getName() + wrapped + "  \"" + text + "\"");
							return null;
						}

						@Override public boolean shouldTraverse(Object o) {
							return true;
						}

						// Depth first
						@Override public void walkJAXBElements(Object parent) {

							indent += "    ";

							List children = getChildren(parent);
							if (children != null) {

								for (Object o : children) {

									this.apply(o);

									// if its wrapped in javax.xml.bind.JAXBElement, get its
									// value
									o = XmlUtils.unwrap(o);

									if (this.shouldTraverse(o)) {
										walkJAXBElements(o);
									}

								}
							}

							indent = indent.substring(0, indent.length() - 4);
						}

						@Override public List<Object> getChildren(Object o) {
							return TraversalUtil.getChildrenImpl(o);
						}

						public void processSubClass(Object o, FormField formFieldDto) {

							ArrayList<String> lString = new ArrayList<>();
							//							Object o = o;
							switch (o.getClass().getSimpleName()) {
								case "Alias":
									lString.add(((org.docx4j.wml.SdtPr.Alias) o).getVal());

									formFieldDto.setLabel(lString.get(0));
									log.info("o = " + o.getClass().getName() + " " + lString.get(0));

									break;
								case "Tag":
									lString.add(((org.docx4j.wml.Tag) o).getVal());

									if (formFieldDto.getLabel() == null || formFieldDto.getLabel().isEmpty()) {
										formFieldDto.setLabel(lString.get(0));
									}
									log.info("o = " + o.getClass().getName() + " " + lString.get(0));

									break;
								case "Id":
									lString.add(((org.docx4j.wml.Id) o).getVal().toString());

									formFieldDto.setKey(lString.get(0));
									log.info("o = " + o.getClass().getName() + " " + lString.get(0));

									break;
								case "CTPlaceholder":
									lString.add(((org.docx4j.wml.CTPlaceholder) o).getDocPart().getVal());
									log.info("o = " + o.getClass().getName() + " " + lString.get(0));
									break;
								case "BooleanDefaultTrue":
									lString.add(o.toString());
									log.info("o = " + o.getClass().getName() + " " + lString.get(0));
									break;
								case "CTSdtComboBox":
									List<CTSdtListItem> lComboElements = ((org.docx4j.wml.CTSdtComboBox) o)
											.getListItem();
									if (!lComboElements.isEmpty()) {
										formFieldDto.setType(Type.select);
										formFieldDto.setSubType(SubType.text);
										List<FieldOption> fieldOptionList = new ArrayList<>();
										for (CTSdtListItem lCombo : lComboElements) {
											lString.add(lCombo.getDisplayText());
											FieldOption fieldOption = FieldOption.builder()
													.value(lCombo.getDisplayText()).build();
											fieldOptionList.add(fieldOption);
											log.info("o = " + o.getClass().getName() + " " + lCombo.getDisplayText());

										}
										formFieldDto.setOptions(fieldOptionList);
									}

									break;
							}
						}
					}

			);
		}
	}

	/**
	 * Identify all the Placeholders in the MS Doc file
	 *
	 * @param str MS Document(un-marshalled to String by DOCX4J library)
	 * @return List of Tags
	 */
	private List<String> getTagValues(String str) {
		final List<String> tagValues = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			tagValues.add(matcher.group());
		}
		return tagValues;
	}

	/**
	 * Parser of a Tag
	 *
	 * @param textToCrawl   Tag
	 * @param patternString pattern
	 * @param groupId       pattern group
	 * @return
	 */
	private String patternMatcher(String textToCrawl, String patternString, int groupId) {
		String value = "";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(textToCrawl);
		while (matcher.find()) {
			value = matcher.group(groupId);
		}
		return value;
	}
}
