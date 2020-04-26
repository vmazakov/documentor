package bg.documentor.util;

import bg.documentor.model.FormFieldHist;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.util.List;

public interface WordDocumentProcessor {

	WordprocessingMLPackage getWordProcessingMLPackage(File fileToParse);

	void parseWordDocGamma(File fileToParse);

	void parseWordDocTeta(File fileToParse);

	void generateMSwordFile(List<FormFieldHist> formFieldHistList);

	void generateWordFile(List<FormFieldHist> formFieldHistList);

	void parseWordDocBeta(File fileToParse);
}
