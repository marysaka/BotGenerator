package eu.thog92.generator.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc...
 * Created by Thog the 28/05/2016
 */
public final class MineTypeDatabase
{
    private MineTypeDatabase()
    {

    }

    public static String getExtension(String fileName)
    {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i >= 0)
        {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    private static final Map<String, String> TYPES = new HashMap<>();

    public static String getMineType(File extension)
    {
        return TYPES.getOrDefault(getExtension(extension.getName()), "application/octet-stream");
    }

    static
    {
        // application/
        TYPES.put("a", "application/octet-stream");
        TYPES.put("ai", "application/postscript");
        TYPES.put("asc", "application/pgp-signature");
        TYPES.put("atom", "application/atom+xml");
        TYPES.put("bcpio", "application/x-bcpio");
        TYPES.put("bin", "application/octet-stream");
        TYPES.put("bz2", "application/x-bzip2");
        TYPES.put("cab", "application/vnd.ms-cab-compressed");
        TYPES.put("chm", "application/vnd.ms-htmlhelp");
        TYPES.put("class", "application/octet-stream");
        TYPES.put("cdf", "application/x-netcdf");
        TYPES.put("cpio", "application/x-cpio");
        TYPES.put("csh", "application/x-csh");
        TYPES.put("deb", "application/x-deb");
        TYPES.put("dll", "application/octet-stream");
        TYPES.put("dmg", "application/x-apple-diskimage");
        TYPES.put("doc", "application/msword");
        TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        TYPES.put("dot", "application/msword");
        TYPES.put("dvi", "application/x-dvi");
        TYPES.put("eps", "application/postscript");
        TYPES.put("exe", "application/octet-stream");
        TYPES.put("gtar", "application/x-gtar");
        TYPES.put("gz", "application/x-gzip");
        TYPES.put("hdf", "application/x-hdf");
        TYPES.put("hqx", "application/mac-binhex40");
        TYPES.put("iso", "application/octet-stream");
        TYPES.put("jar", "application/java-archive");
        TYPES.put("js", "application/javascript");
        TYPES.put("json", "application/json");
        TYPES.put("latex", "application/x-latex");
        TYPES.put("man", "application/x-troff-man");
        TYPES.put("me", "application/x-troff-me");
        TYPES.put("mif", "application/x-mif");
        TYPES.put("ms", "application/x-troff-ms");
        TYPES.put("nc", "application/x-netcdf");
        TYPES.put("o", "application/octet-stream");
        TYPES.put("obj", "application/octet-stream");
        TYPES.put("oda", "application/oda");
        TYPES.put("odt", "application/vnd.oasis.opendocument.text");
        TYPES.put("odp", "application/vnd.oasis.opendocument.presentation");
        TYPES.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        TYPES.put("odg", "application/vnd.oasis.opendocument.graphics");
        TYPES.put("p12", "application/x-pkcs12");
        TYPES.put("p7c", "application/pkcs7-mime");
        TYPES.put("pdf", "application/pdf");
        TYPES.put("pfx", "application/x-pkcs12");
        TYPES.put("pgp", "application/pgp-encrypted");
        TYPES.put("pot", "application/vnd.ms-powerpoint");
        TYPES.put("ppa", "application/vnd.ms-powerpoint");
        TYPES.put("pps", "application/vnd.ms-powerpoint");
        TYPES.put("ppt", "application/vnd.ms-powerpoint");
        TYPES.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        TYPES.put("ps", "application/postscript");
        TYPES.put("pwz", "application/vnd.ms-powerpoint");
        TYPES.put("pyc", "application/x-python-code");
        TYPES.put("pyo", "application/x-python-code");
        TYPES.put("ram", "application/x-pn-realaudio");
        TYPES.put("rar", "application/x-rar-compressed");
        TYPES.put("rdf", "application/rdf+xml");
        TYPES.put("rpm", "application/x-redhat-package-manager");
        TYPES.put("rss", "application/rss+xml");
        TYPES.put("rtf", "application/rtf");
        TYPES.put("roff", "application/x-troff");
        TYPES.put("sh", "application/x-sh");
        TYPES.put("shar", "application/x-shar");
        TYPES.put("sig", "application/pgp-signature");
        TYPES.put("sit", "application/x-stuffit");
        TYPES.put("smil", "application/smil+xml");
        TYPES.put("so", "application/octet-stream");
        TYPES.put("src", "application/x-wais-source");
        TYPES.put("sv4cpio", "application/x-sv4cpio");
        TYPES.put("sv4crc", "application/x-sv4crc");
        TYPES.put("swf", "application/x-shockwave-flash");
        TYPES.put("t", "application/x-troff");
        TYPES.put("tar", "application/x-tar");
        TYPES.put("tcl", "application/x-tcl");
        TYPES.put("tex", "application/x-tex");
        TYPES.put("texi", "application/x-texinfo");
        TYPES.put("texinfo", "application/x-texinfo");
        TYPES.put("torrent", "application/x-bittorrent");
        TYPES.put("tr", "application/x-troff");
        TYPES.put("ustar", "application/x-ustar");
        TYPES.put("wiz", "application/msword");
        TYPES.put("wsdl", "application/wsdl+xml");
        TYPES.put("xht", "application/xhtml+xml");
        TYPES.put("xhtml", "application/xhtml+xml");
        TYPES.put("xlb", "application/vnd.ms-excel");
        TYPES.put("xls", "application/vnd.ms-excel");
        TYPES.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        TYPES.put("xpdl", "application/xml");
        TYPES.put("xsl", "application/xml");
        TYPES.put("xul", "application/vnd.mozilla.xul+xml");
        TYPES.put("zip", "application/zip");

        // audio/
        TYPES.put("aif", "audio/x-aiff");
        TYPES.put("aifc", "audio/x-aiff");
        TYPES.put("aiff", "audio/x-aiff");
        TYPES.put("au", "audio/basic");
        TYPES.put("flac", "audio/x-flac");
        TYPES.put("mid", "audio/midi");
        TYPES.put("midi", "audio/midi");
        TYPES.put("mp2", "audio/mpeg");
        TYPES.put("mp3", "audio/mpeg");
        TYPES.put("m3u", "audio/x-mpegurl");
        TYPES.put("oga", "audio/ogg");
        TYPES.put("ogg", "audio/ogg");
        TYPES.put("ra", "audio/x-pn-realaudio");
        TYPES.put("snd", "audio/basic");
        TYPES.put("wav", "audio/x-wav");

        // image/
        TYPES.put("bmp", "image/x-ms-bmp");
        TYPES.put("djv", "image/vnd.djvu");
        TYPES.put("djvu", "image/vnd.djvu");
        TYPES.put("gif", "image/gif");
        TYPES.put("ico", "image/vnd.microsoft.icon");
        TYPES.put("ief", "image/ief");
        TYPES.put("jpe", "image/jpeg");
        TYPES.put("jpeg", "image/jpeg");
        TYPES.put("jpg", "image/jpeg");
        TYPES.put("pbm", "image/x-portable-bitmap");
        TYPES.put("pgm", "image/x-portable-graymap");
        TYPES.put("png", "image/png");
        TYPES.put("pnm", "image/x-portable-anymap");
        TYPES.put("ppm", "image/x-portable-pixmap");
        TYPES.put("psd", "image/vnd.adobe.photoshop");
        TYPES.put("ras", "image/x-cmu-raster");
        TYPES.put("rgb", "image/x-rgb");
        TYPES.put("svg", "image/svg+xml");
        TYPES.put("svgz", "image/svg+xml");
        TYPES.put("tif", "image/tiff");
        TYPES.put("tiff", "image/tiff");
        TYPES.put("xbm", "image/x-xbitmap");
        TYPES.put("xpm", "image/x-xpixmap");
        TYPES.put("xwd", "image/x-xwindowdump");

        // message/
        TYPES.put("eml", "message/rfc822");
        TYPES.put("mht", "message/rfc822");
        TYPES.put("mhtml", "message/rfc822");
        TYPES.put("nws", "message/rfc822");

        // model/
        TYPES.put("vrml", "model/vrml");

        // text/
        TYPES.put("asm", "text/x-asm");
        TYPES.put("bat", "text/plain");
        TYPES.put("c", "text/x-c");
        TYPES.put("cc", "text/x-c");
        TYPES.put("conf", "text/plain");
        TYPES.put("cpp", "text/x-c");
        TYPES.put("css", "text/css");
        TYPES.put("csv", "text/csv");
        TYPES.put("diff", "text/x-diff");
        TYPES.put("etx", "text/x-setext");
        TYPES.put("gemspec", "text/x-ruby");
        TYPES.put("h", "text/x-c");
        TYPES.put("hh", "text/x-c");
        TYPES.put("htm", "text/html");
        TYPES.put("html", "text/html");
        TYPES.put("ics", "text/calendar");
        TYPES.put("java", "text/x-java");
        TYPES.put("ksh", "text/plain");
        TYPES.put("lua", "text/x-lua");
        TYPES.put("manifest", "text/cache-manifest");
        TYPES.put("md", "text/x-markdown");
        TYPES.put("p", "text/x-pascal");
        TYPES.put("pas", "text/x-pascal");
        TYPES.put("pl", "text/x-perl");
        TYPES.put("pm", "text/x-perl");
        TYPES.put("py", "text/x-python");
        TYPES.put("rb", "text/x-ruby");
        TYPES.put("ru", "text/x-ruby");
        TYPES.put("rockspec", "text/x-lua");
        TYPES.put("rtx", "text/richtext");
        TYPES.put("s", "text/x-asm");
        TYPES.put("sgm", "text/x-sgml");
        TYPES.put("sgml", "text/x-sgml");
        TYPES.put("text", "text/plain");
        TYPES.put("tsv", "text/tab-separated-values");
        TYPES.put("txt", "text/plain");
        TYPES.put("vcf", "text/x-vcard");
        TYPES.put("vcs", "text/x-vcalendar");
        TYPES.put("xml", "text/xml");
        TYPES.put("yaml", "text/yaml");
        TYPES.put("yml", "text/yml");

        // video/
        TYPES.put("avi", "video/x-msvideo");
        TYPES.put("flv", "video/x-flv");
        TYPES.put("m1v", "video/mpeg");
        TYPES.put("mov", "video/quicktime");
        TYPES.put("movie", "video/x-sgi-movie");
        TYPES.put("mng", "video/x-mng");
        TYPES.put("mp4", "video/mp4");
        TYPES.put("mpa", "video/mpeg");
        TYPES.put("mpe", "video/mpeg");
        TYPES.put("mpeg", "video/mpeg");
        TYPES.put("mpg", "video/mpeg");
        TYPES.put("ogv", "video/ogg");
        TYPES.put("qt", "video/quicktime");
    }
}
