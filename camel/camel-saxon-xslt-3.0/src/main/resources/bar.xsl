<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="3.0">
    <xsl:output method="xml" version="1.0" encoding="UTF-8"/>
    <xsl:template match="tag1">
        <tag1-transformed>
            <xsl:apply-templates/>
        </tag1-transformed>
    </xsl:template>
</xsl:stylesheet>
