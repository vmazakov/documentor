package bg.documentor.util.impl;

import bg.documentor.util.Handler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j @Component @NoArgsConstructor @AllArgsConstructor public class IntelliCrawler {

	public static final String TEXT_NODES_X_PATH = "//w:sdt";
	/**
	 * Map: key = Class, value: Object related method
	 */
	private static final Map<Class, Handler> dispatch = new HashMap<>();

	/**
	 * Populate the dispatch Map in a static initializer
	 */
	static {

		dispatch.put(Text.class, o -> getClassNameText((Text) o));
		dispatch.put(Id.class, o -> getClassNameId((Id) o));
		dispatch.put(Tag.class, o -> getClassNameTag((Tag) o));
		dispatch.put(RPr.class, o -> getClassNameRPr((RPr) o));
		dispatch.put(SdtPr.Alias.class, o -> getClassNameAlias((SdtPr.Alias) o));
		dispatch.put(CTPlaceholder.class, o -> getClassNameCTPlaceholder((CTPlaceholder) o));
		dispatch.put(BooleanDefaultTrue.class, o -> getClassNameBooleanDefaultTrue((BooleanDefaultTrue) o));
		dispatch.put(CTSdtDate.class, o -> getClassNameCTSdtDate((CTSdtDate) o));
		dispatch.put(CTSdtDropDownList.class, o -> getClassNameCTSdtDropDownList((CTSdtDropDownList) o));

	}

	public MainDocumentPart documentPart = null;

	//	/**
	////	 * Finder using Traverse
	////	 *
	////	 * @param parent Parent object
	////	 * @param cl     Class to be found
	////	 * @return list of Objects
	////	 */
	////	private static List<Object> newFinder(Object parent, Class cl) {
	////		ClassFinder finder = new ClassFinder(cl);
	////		TraversalUtil traversalUtil = new TraversalUtil(parent, finder);
	////		return (finder.results);
	////	}

	/**
	 * get properties
	 *
	 * @param obj Parent object
	 * @return list of objects
	 */
	private static List<Object> getSdtProperties(Object obj) {
		/**
		 * get sdt properties
		 */
		Object sdtPropertiesObject = ((org.docx4j.wml.SdtElement) obj).getSdtPr();
		/**
		 * get nested objects in sdtPr
		 */
		List sdtPropertiesList = ((SdtPr) sdtPropertiesObject).getRPrOrAliasOrLock();

		return (sdtPropertiesList);
	}

	/**
	 * The following concept of using static initializer, dispatch(Map) and a handler is probably the best alternative of a long "if/else if (instanceof)" block
	 * source(https://stackoverflow.com/questions/29570767/switch-over-type-in-java/45068355)
	 * alternative: Pattern matching for switch (https://openjdk.java.net/jeps/8213076)
	 * !Visitor Design Pattern not applicable!
	 *
	 * @param o Object to be identified
	 * @return string(Class name) used later as a key in a Map
	 */
	private static String handle(Object o) {
		Handler h = dispatch.get(o.getClass());
		if (h == null) {
			/**
			 * Throw an exception: unknown type
			 */
			return "Any";
		}
		return h.handle(o);
	}

	/**
	 * return 'Text' if Object is instanceof Text
	 *
	 * @param text
	 * @return String Text
	 */
	private static String getClassNameText(Text text) {
		log.info("U should be happy with found Text value: " + text.getValue());
		return "Text";
	}

	/**
	 * return 'Id' if Object is instanceof Id
	 *
	 * @param id
	 * @return String Id
	 */
	private static String getClassNameId(Id id) {
		log.info("U should be happy with found Id value: " + id.getVal());
		return "Id";
	}

	/**
	 * return 'Tag' if Object is instanceof Tag
	 *
	 * @param tag
	 * @return String Tag
	 */
	private static String getClassNameTag(Tag tag) {
		log.info("U should be happy with found Id value: " + tag.getVal());
		return "Tag";
	}

	/**
	 * return 'RPr' if Object is instanceof RPr
	 *
	 * @param rPr
	 * @return String RPr
	 */
	private static String getClassNameRPr(RPr rPr) {
		log.info("U should be happy with found Language value: " + rPr.getLang().getVal());
		return "RPr";
	}

	/**
	 * return 'Alias' if Object is instanceof Alias
	 *
	 * @param alias
	 * @return String Alias
	 */
	private static String getClassNameAlias(SdtPr.Alias alias) {
		log.info("U should be happy with found Alias value: " + alias.getVal());
		return "Alias";
	}

	/**
	 * return 'CTPlaceholder' if Object is instanceof CTPlaceholder
	 *
	 * @param cTPlaceholder
	 * @return String CTPlaceholder
	 */
	private static String getClassNameCTPlaceholder(CTPlaceholder cTPlaceholder) {
		log.info("U should be happy with found DocPart value: " + cTPlaceholder.getDocPart().getVal());
		return "CTPlaceholder";
	}

	/**
	 * return 'BooleanDefaultTrue' if Object is instanceof BooleanDefaultTrue
	 *
	 * @param booleanDefaultTrue
	 * @return String BooleanDefaultTrue
	 */
	private static String getClassNameBooleanDefaultTrue(BooleanDefaultTrue booleanDefaultTrue) {
		log.info("U should be happy with found ShowingPlcHdr(BooleanDefaultTrue) value: " + booleanDefaultTrue.isVal());
		return "BooleanDefaultTrue";
	}

	/**
	 * return 'CTSdtDate' if Object is instanceof CTSdtDate
	 *
	 * @param cTSdtDate
	 * @return String CTSdtDate
	 */
	private static String getClassNameCTSdtDate(CTSdtDate cTSdtDate) {
		log.info("U should be happy with found CTSdtDate value: " + cTSdtDate.getFullDate().getDay() + "/" + cTSdtDate
				.getFullDate().getMonth() + "/" + cTSdtDate.getFullDate().getYear());
		return "CTSdtDate";
	}

	/**
	 * return 'CTSdtDropDownList' if Object is instanceof CTSdtDropDownList
	 *
	 * @param cTSdtDropDownList
	 * @return String CTSdtDropDownList
	 */
	private static String getClassNameCTSdtDropDownList(CTSdtDropDownList cTSdtDropDownList) {
		cTSdtDropDownList.getListItem().forEach(option -> log
				.info("U should be happy with found CTSdtDropDownList values: " + option.getDisplayText() + "/" + option
						.getValue()));
		return "CTSdtDropDownList";
	}

	public Map<BigInteger, Map<String, Object>> mainCrawl(MainDocumentPart documentPart) {
		this.documentPart = documentPart;

		/**
		 * Composite Map: key = Id, value = Map of child objects
		 */
		Map<BigInteger, Map<String, Object>> mapSdtItems = new LinkedHashMap<>();

		/**
		 * get all 'w:sdt' tags
		 */
		List<Object> allSdtObjects = getSdtNodes();

		for (Object o : allSdtObjects) {

			/**
			 * Map: key = Class name, value = Object
			 */
			Map<String, Object> hashSdtItems = new HashMap();

			/**
			 *  in case of type 'SdtRun'
			 */
			if (o instanceof SdtRun) {
				/**
				 * SdtRun has two main sub-branches:
				 *  - SdtPr (SDT Properties)
				 *  - SdtContent (SDT Content)
				 ******************************
				 *  A possible sub-structure could look like:
				 *  - SdtPr (SDT Properties):
				 *  	- rPr
				 *  	- Alias
				 *  	- Tag
				 *  	- Id
				 *  	- Placeholder
				 *  	- BooleanDefaultTrue
				 *  - SdtContent (SDT Content):
				 *  	- R:
				 *  		- rPR
				 *  		- Text
				 ******************************
				 * NB! Some sub-branches should be unwrapped, others - not necessary
				 ******************************
				 * The user's input should be set to the Text object's value. That's sub-branch Text of R of SdtContent.
				 */

				/**
				 * get Sdt Content
				 */
				extractSdtContent(hashSdtItems, o);

				/**
				 * get Sdt Properties
				 */
				extractSdtProperties(hashSdtItems, o);

				/**
				 * put Content and Properties together
				 */
				mapSdtItems.put(((Id) hashSdtItems.get("Id")).getVal(), hashSdtItems);
			}
		}
		return mapSdtItems;
	}

	/**
	 * get all the XML branches, defined as "sdt" i.e. Word's Content Controls
	 *
	 * @return list of objects
	 */

	public List<Object> getSdtNodes() {
		List<Object> sdtNodes = new ArrayList<>();

		try {
			sdtNodes = documentPart.getJAXBNodesViaXPath(TEXT_NODES_X_PATH, true);
		} catch (JAXBException | XPathBinderAssociationIsPartialException e) {
			log.info("Error while getJAXBNodesViaXPath", e);
		}

		if (isNotEmpty(sdtNodes)) {
			sdtNodes = sdtNodes.stream().map(this::unwrapJaxbElement).collect(Collectors.toList());

			sdtNodes.forEach(sdt -> log.info("Boject is: {}", sdt.getClass().getName()));
		}
		return sdtNodes;
	}

	/**
	 * get the SDT's properties
	 *
	 * @param hashSdtItems Map: key = Class name, value = Object
	 * @param o            Parent object
	 */
	void extractSdtProperties(Map<String, Object> hashSdtItems, Object o) {

		List<Object> sdtPropertiesList = getSdtProperties(o);

		sdtPropertiesList.stream().map(this::unwrapJaxbElement).forEach(nestedObject -> {
			String className = handle(nestedObject);
			hashSdtItems.put(className, nestedObject);
		});

	}

	/**
	 * get SDT's content
	 *
	 * @param hashSdtItems Map: key = Class name, value = Object
	 * @param o            Parent object
	 */
	private void extractSdtContent(Map<String, Object> hashSdtItems, Object o) {

		/**
		 *  get SDT content
		 */
		IntelliCrawler.log.info("The found Content is: " + ((SdtRun) o).getSdtContent());
		/**
		 * getContent returns List: variant A
		 */
		//				ArrayListWml arrayListWml = (ArrayListWml) ((SdtRun) o).getSdtContent().getContent();
		/**
		 * getContent returns List: variant B => it is expected just one Run in SDT, so we get it via get(0)
		 */
		Object obj = ((org.docx4j.wml.SdtElement) o).getSdtContent().getContent().get(0);
		List arrayListWml = ((R) obj).getContent();
		/**
		 *
		 * (it is expected just one Text in a Run, so we get it via get(0) OR traverse the list)
		 */
		arrayListWml.stream().map(this::unwrapJaxbElement).forEach(nestedObject -> {
			String className = handle(nestedObject);
			hashSdtItems.put(className, nestedObject);
		});
	}

	/**
	 * That's an un-wrapper
	 *
	 * @param o Parent object
	 * @return unwrapped object
	 */
	private Object unwrapJaxbElement(Object o) {
		if (o instanceof javax.xml.bind.JAXBElement) {
			return ((JAXBElement) o).getValue();
		}
		return o;
	}

}
