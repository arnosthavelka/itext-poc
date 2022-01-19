package com.github.aha.poc.itext;

import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA;
import static com.itextpdf.kernel.colors.ColorConstants.BLUE;
import static com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor.getTextFromPage;
import static com.itextpdf.layout.properties.TextAlignment.CENTER;
import static com.itextpdf.layout.properties.VerticalAlignment.TOP;
import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

class WatermarkTests extends AbstractPdfTest {

	static final String SOURCE_PDF = "simple.pdf";

	@Test
	void generateWatermarkToPdf() throws IOException {
		String targetPdf = RESULT_PATH + "/example-watermark-generated.pdf";
		var text1 = """
				Duisnetus ceteros proin posuere at invidunt purus minim adolescens vehicula.  Sempervero graeci definiebas definitiones errem egestas utamur assueverit delectus quem esse meliore neglegentur porta neque signiferumque ius elitr.  Dictassaperet scripta perpetua habeo risus tation definitiones fames expetendis verterem alienum duo erroribus detracto.
				Fabellasalia has simul litora ornatus recteque senserit morbi evertitur inani voluptaria fuisset referrentur qui.  Voluptariadisputationi ridens indoctum delectus fabellas urna eripuit vulputate porttitor evertitur alterum ignota inciderint justo latine dico duo melius aptent.  Dicoponderum quaerendum fusce tibique habitasse posse quem quod nisi elitr blandit placerat suavitate.  Atnunc voluptatum nihil quisque nominavi dolorum intellegat natum eius molestiae iusto sententiae melius eos quaerendum primis decore maximus praesent.  Aequeac non curabitur sodales dapibus placerat dicam ferri.
				Vestibulumoption evertitur menandri inimicus mauris nonumy appareat conceptam definitiones nullam invidunt dicta ocurreret nascetur porta sapien.  Facilisidelicata luctus conclusionemque sodales pharetra iuvaret option evertitur error velit invidunt tristique evertitur an pertinax integer adolescens facilis tota.  Repudiandaepulvinar ad.  Meldictas suscipiantur definitionem dis moderatius porttitor adipiscing recteque scripta.  Causaealiquam periculis pertinacia facilisi elaboraret postulant venenatis veri docendi agam pertinacia habemus mollis lobortis harum.  Sapientemarcu vehicula consectetuer ocurreret legimus elementum agam.
				Duiselectram sollicitudin rhoncus montes contentiones postulant sagittis omittam alterum equidem.  Adversariumligula iisque verterem efficitur patrioque molestiae gloriatur elementum vocent.
				Ridensequidem pharetra tristique dicam.  Intellegebatnostrum iisque habemus platonem iriure sociosqu novum eloquentiam nonumy mentitum instructior postea appetere vidisse constituto dolor scelerisque delicata conclusionemque.  Ornatusomnesque tota magna habeo mediocritatem felis tempor splendide molestiae hinc.  Fuissetmediocritatem torquent partiendo delectus mel tempor quo semper pellentesque maluisset vituperatoribus lacus class mus.  Appeteresonet eum decore numquam atqui luptatum vis his expetenda viverra aliquet necessitatibus fuisset malesuada mandamus parturient rhoncus atqui.  Euismodquod facilisis scripserit pulvinar maecenas esse facilis alienum ut netus tortor moderatius scripserit.
				Interessetfacilisis dico dictum elaboraret.  Librissagittis vero ubique adversarium quidam pretium molestie sagittis mazim contentiones venenatis gravida sem alienum regione nihil porttitor mauris.  Constituamneque constituam.
				""";
		var text2 = """
				Mattisne dui imperdiet mea suscipiantur pri suas evertitur duo suscipit.  Proinfeugait est.  Quasdolore lacinia tale pellentesque legere mea atomorum sumo no felis dicam intellegebat curabitur maiestatis sumo malesuada appetere mea.  Oporteatpropriae facilis iriure nunc neque omnesque purus persecuti eleifend interesset iuvaret imperdiet.  Euripidiseuismod pertinax ponderum dictumst tempus morbi.
				Epicurivocibus propriae detraxit sed sonet vis mei congue dolorem quam eirmod platea saepe appetere ceteros.  Crasubique ubique.  Blanditcurabitur nostra aptent mutat parturient interpretaris ut dicat posse ultrices gloriatur no ceteros montes causae.  Veritusmel possit facilis possit tristique epicurei nonumy volumus veritus id per legimus petentium qui definitionem.
				Voluptatibusbibendum postea porta taciti dictas convenire malesuada atqui dui ocurreret noluisse natoque constituto tortor orci netus.  Ignotautinam maluisset in vis morbi est reprehendunt offendit posidonium aliquip accommodare agam sadipscing graeco.
				Bibendumadipisci felis pellentesque pertinacia honestatis erat quem erat porttitor mollis iusto sonet error et.  Intellegebatpraesent reprehendunt constituto splendide posidonium vero dissentiunt repudiandae sagittis.  Pharetravelit euripidis.  Doloreet sociosqu unum.  Senectusper iudicabit oratio congue persequeris interpretaris consectetur causae delenit convenire aliquet libris mel maluisset nonumes possim.
				Neea senserit audire iusto aperiri ut pertinacia iudicabit vocibus moderatius proin.  Requetheophrastus recteque postulant an repudiandae constituto disputationi elitr invidunt quo omittam postulant.  Arcuperpetua graecis proin sit elitr explicari assueverit mattis vis sanctus ea.
				Hasvolumus gubergren consul.  Pertinaciautinam vivendo pulvinar odio sagittis reformidans offendit civibus ultricies maiorum voluptaria tacimates himenaeos ei tortor solet odio eos enim.  Elementumdelectus a nostrum magna eu autem eius aptent reformidans gravida propriae his movet deterruisset.  Maiorumproin volumus porttitor wisi mus.
				Posteanam commune consequat verear vocibus sit vulputate proin mutat ferri finibus dicunt tristique an diam errem his delectus.  Adversariumporro volutpat voluptatibus vocibus nobis cum qui integer mi.  Utauctor penatibus est simul magnis pericula delectus quidam dignissim sodales dolorum invenire ac tellus sociosqu viris repudiare iuvaret.  Referrenturante parturient omnesque graece habitant tota elementum nullam gravida mel utamur usu.  Egetdolore felis quisque laoreet magna explicari noluisse orci habemus sanctus ne error brute suscipiantur vestibulum ut evertitur.
				""";
		var watermark = "PREVIEW";
		var textStyle = TextStyle.builder()
				.fontFamily(HELVETICA)
				.fontSize(100f)
				.rotationInDegrees(45f)
				.opacity(0.5f)
				.build();

		try (PdfWriter writer = new PdfWriter(targetPdf);
				PdfDocument pdfDocument = new PdfDocument(writer);
				Document document = new Document(pdfDocument)) {

			document.add(new Paragraph(text1));
			document.add(new Paragraph(text2));

			var paragraph = createWatermarkParagraph(watermark, textStyle);
			for (int i = 1; i <= document.getPdfDocument().getNumberOfPages(); i++) {
				addWatermarkToPage(document, i, paragraph, textStyle, 0f);
			}
		}

		verifyPreviewWatermark(targetPdf, watermark);
	}

	void addWatermarkToPage(Document document, int pageIndex, Paragraph paragraph, TextStyle textStyle, float verticalOffset) {
		var pdfDoc = document.getPdfDocument();
		var pdfPage = pdfDoc.getPage(pageIndex);
		var pageSize = pdfPage.getPageSizeWithRotation();

		float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
		float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
		float xOffset = textStyle.getFontSize() / 2;
		float rotationInRadians = (float) (PI / 180 * textStyle.getRotationInDegrees());
		document.showTextAligned(paragraph, x - xOffset, y + verticalOffset, pageIndex, CENTER, TOP, rotationInRadians);
	}

	@Test
	void addWatermarkToExistingPdf() throws IOException {
		String targetPdf = RESULT_PATH + "/example-watermark-modified.pdf";
		var watermark = "CONFIDENTIAL";
		var textStyle = TextStyle.builder()
				.color(BLUE)
				.fontFamily(HELVETICA)
				.fontSize(50f)
				.rotationInDegrees(20f)
				.opacity(0.5f)
				.build();

		try (var pdfDoc = new PdfDocument(new PdfReader(SOURCE_PDF), new PdfWriter(targetPdf))) {
			var document = new Document(pdfDoc);

			var paragraph = createWatermarkParagraph(watermark, textStyle);
			var transparentGraphicState = new PdfExtGState().setFillOpacity(0.5f);
			for (int i = 1; i <= document.getPdfDocument().getNumberOfPages(); i++) {
				addWatermarkToPage(document, i, paragraph, transparentGraphicState, textStyle, 330f);
			}
		}

		verifyPreviewWatermark(targetPdf, watermark);
	}

	void addWatermarkToPage(Document document, int pageIndex, Paragraph paragraph, PdfExtGState graphicState, TextStyle textStyle,
			float verticalOffset) {
		var pdfDoc = document.getPdfDocument();
		var pdfPage = pdfDoc.getPage(pageIndex);
		var pageSize = pdfPage.getPageSizeWithRotation();

		float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
		float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
		var over = new PdfCanvas(pdfDoc.getPage(pageIndex));
		over.saveState();
		over.setExtGState(graphicState);
		float xOffset = textStyle.getFontSize() / 2;
		float rotationInRadians = (float) (PI / 180 * textStyle.getRotationInDegrees());
		document.showTextAligned(paragraph, x - xOffset, y + verticalOffset, pageIndex, CENTER, TOP, rotationInRadians);
		document.flush();
		over.restoreState();
		over.release();
	}

	Paragraph createWatermarkParagraph(String watermark, TextStyle textStyle) {
		var text = new Text(watermark);
		text.setFont(createFont(textStyle.getFontFamily()));
		text.setFontSize(textStyle.getFontSize());
		text.setFontColor(textStyle.getColor());
		text.setOpacity(textStyle.getOpacity());
		return new Paragraph(text);
	}

	PdfFont createFont(String fontFamily) {
		try {
			return PdfFontFactory.createFont(fontFamily);
		} catch (IOException e) {
			throw new PdfException("Font creation failed", e);
		}
	}

	void verifyPreviewWatermark(String targetPdf, String watermark) throws IOException {
		var extStrategy = new LocationTextExtractionStrategy();
		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
				var textFromPage = getTextFromPage(pdfDocument.getPage(i), extStrategy);
				assertThat(textFromPage).contains(watermark);
			}
		}
	}

}
