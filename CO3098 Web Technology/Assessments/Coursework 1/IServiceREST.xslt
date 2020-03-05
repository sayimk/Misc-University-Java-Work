<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
	<html>
		<body>
			<table border="1">
				<caption> 	&#60;Interface&#62; <br/><xsl:value-of select="/service/@id"/></caption>

				<xsl:for-each select="/service/abstract_method">
					<tr>
						<td>
							
							<xsl:value-of select="@id"/>
							(
							<xsl:for-each select="parameters/argument">
							<xsl:value-of select="@type"/>
							:
							<xsl:value-of select="."/>
							<xsl:if test="position() != last()">, </xsl:if> 
							</xsl:for-each>)
						</td>				
						<td>
							<xsl:value-of select="return"/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</body>
	</html>
</xsl:template>
</xsl:stylesheet>