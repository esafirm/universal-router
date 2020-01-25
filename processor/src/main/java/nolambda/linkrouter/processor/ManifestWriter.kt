package nolambda.linkrouter.processor

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.File
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * Sample of Provider
 * <provider
 * android:name="nolambda.linkrouter.product.ProductRouterInitializer"
 * android:authorities="nolambda.router.product"
 * android:exported="false" />
 */
class ManifestWriter(
    private val manifestFile: File,
    private val providers: List<ProviderNode>
) {

    companion object {
        private const val TAG_APPLICATION = "application"
        private const val TAG_PROVIDER = "provider"

        private const val ATTR_NAME = "android:name"
        private const val ATTR_AUTHORITIES = "android:authorities"
        private const val ATTR_EXPORTED = "android:exported"
    }

    fun writeOut(): String {
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(manifestFile.reader())
        val doc = dBuilder.parse(xmlInput)

        providers.forEach { node ->
            val application = doc.getElementsByTagName(TAG_APPLICATION).item(0)
            val provider = doc.createProviderElement(node)
            application.appendChild(provider)
        }

        return doc.stringify()
    }

    private fun Document.createProviderElement(node: ProviderNode): Element {
        return createElement(TAG_PROVIDER).apply {
            setAttribute(ATTR_NAME, node.className)
            setAttribute(ATTR_AUTHORITIES, node.authorities)
            setAttribute(ATTR_EXPORTED, "false")
        }
    }

    private fun Document.stringify(): String {
        val domSource = DOMSource(this)
        val writer = StringWriter()
        val result = StreamResult(writer)
        val tf = TransformerFactory.newInstance()
        val transformer = tf.newTransformer().apply {
            setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            setOutputProperty(OutputKeys.INDENT, "yes")
        }
        transformer.transform(domSource, result)
        return writer.toString()
    }
}