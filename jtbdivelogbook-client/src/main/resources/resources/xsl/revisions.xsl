<?xml version="1.0" encoding="UTF-8"?>

<!-- Document : revisions.xsl Created on : February 11, 2011, 7:16 PM Author 
	: gautier -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="html" />

	<xsl:template match="/">
		<html>
			<head>
				<title>Revisions</title>
				<style type="text/css">
					div{
					position:relative;
					margin-right:auto;margin-left:auto;
					width:90%
					}
					table, td{
					border-collapse:collapse;
					border:1px solid grey;
					vertical-align:top;
					font-family:"Verdana";
					font-size:.6em;
					padding:5px;
					}

					td.number{
					border-left:2px solid black;
					border-top:2px solid black;
					border-bottom:2px solid black;
					}

					td.resolution{
					border-bottom:2px
					solid black;
					}

					p.level{
					font-size:0.6em;
					font-style:italic;
					text-align:right;
					}

					td.registerdate{
					border-right:2px solid black;
					}

					td.title{
					border-top:2px solid black;
					font-size:1.1em;
					background-color:#DDDDDD;
					}

					td.notsolved{
					background-color:#F43030;
					border-right:2px solid black;
					border-bottom:2px solid black;
					border-top:2px solid black;
					width:90px;
					}

					td.solved{
					background-color:#00F400;
					border-right:2px solid black;
					border-top:2px solid black;
					border-bottom:2px solid black;
					width:90px;
					}

					h2{
					font-size:1.7em;
					}

					h3{
					font-size:1.3em;
					}      
			 </style>
			</head>

			<body>
				<h1 align="center">
					<i>JtB Dive Logbook Revisions</i>
				</h1>

				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<xsl:template match="version">

		<h2>
			<u>
				Version :
				<xsl:value-of select="@id" />
			</u>
		</h2>

		# changes:
		<xsl:value-of select="count(./change/level[.='change'])" />
		- # bugfix:
		<xsl:value-of select="count(./change/level[.='bugfix'])" />
		<div>
			<xsl:apply-templates select="change">
				<xsl:sort select="@id" order="descending" data-type="number" />
			</xsl:apply-templates>
		</div>

	</xsl:template>

	<xsl:template match="change">
		<xsl:param name="lvl">
			<xsl:value-of select="level" />
		</xsl:param>
		<ul>
			<li>
				[
				<xsl:value-of select="@id" />
				]

				<xsl:choose>
					<xsl:when test="$lvl='change'">
						Change
					</xsl:when>
					<xsl:when test="$lvl='bugfix'">
						BugFix
					</xsl:when>
				</xsl:choose>

				(
				<xsl:value-of select="@date" />
				) :
				<xsl:value-of select="title" />
				<br />
				<u>Description:</u>
				<xsl:value-of select="description" />
				<br />
				<u>Resolution</u>
				(
				<xsl:value-of select="resolution/@date" />
				):
				<xsl:value-of select="resolution" />
			</li>
		</ul>
	</xsl:template>


</xsl:stylesheet>