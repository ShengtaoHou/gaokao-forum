/*
 * Symphony - A modern community (forum/SNS/blog) platform written in Java.
 * Copyright (C) 2012-2017,  b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.b3log.symphony.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.options.DataHolder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Latkes;
import org.b3log.latke.cache.Cache;
import org.b3log.latke.cache.CacheFactory;
import org.b3log.latke.ioc.LatkeBeanManager;
import org.b3log.latke.ioc.LatkeBeanManagerImpl;
import org.b3log.latke.ioc.Lifecycle;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.jdbc.JdbcRepository;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.LangPropsServiceImpl;
import org.b3log.latke.util.Callstacks;
import org.b3log.latke.util.MD5;
import org.b3log.latke.util.Stopwatchs;
import org.b3log.latke.util.Strings;
import org.b3log.symphony.service.UserQueryService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * <a href="http://en.wikipedia.org/wiki/Markdown">Markdown</a> utilities.
 * <p>
 * Uses the <a href="https://github.com/chjj/marked">marked</a> as the processor, if not found this command, try
 * built-in <a href="https://github.com/vsch/flexmark-java">flexmark</a> instead.
 * </p>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @author <a href="http://zephyr.b3log.org">Zephyr</a>
 * @author <a href="http://vanessa.b3log.org">Vanessa</a>
 * @version 1.11.17.23, Jun 1, 2017
 * @since 0.2.0
 */
public final class Markdowns {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(Markdowns.class);

    /**
     * Language service.
     */
    private static final LangPropsService LANG_PROPS_SERVICE
            = LatkeBeanManagerImpl.getInstance().getReference(LangPropsServiceImpl.class);

    /**
     * Bean manager.
     */
    private static final LatkeBeanManager beanManager = Lifecycle.getBeanManager();

    /**
     * User query service.
     */
    private static final UserQueryService userQueryService;

    /**
     * Markdown cache.
     */
    private static final Cache MD_CACHE = CacheFactory.getCache("markdown");

    /**
     * Markdown to HTML timeout.
     */
    private static final int MD_TIMEOUT = 2000;

    /**
     * Marked engine serve path.
     */
    private static final String MARKED_ENGINE_URL = "http://localhost:8250";

    /**
     * Built-in MD engine options.
     */

    private static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL_OPTIONALS | Extensions.ALL_WITH_OPTIONALS
    );

    /**
     * Built-in MD engine parser.
     */
    private static final com.vladsch.flexmark.parser.Parser PARSER =
            com.vladsch.flexmark.parser.Parser.builder(OPTIONS).build();

    /**
     * Built-in MD engine HTML renderer.
     */
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();

    /**
     * Whether marked is available.
     */
    public static boolean MARKED_AVAILABLE;

    static {
        MD_CACHE.setMaxCount(1024 * 10 * 4);

        if (null != beanManager) {
            userQueryService = beanManager.getReference(UserQueryService.class);
        } else {
            userQueryService = null;
        }
    }

    static {
        try {
            final URL url = new URL(MARKED_ENGINE_URL);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);

            final OutputStream outputStream = conn.getOutputStream();
            IOUtils.write("Symphony 大法好", outputStream, "UTF-8");
            IOUtils.closeQuietly(outputStream);

            final InputStream inputStream = conn.getInputStream();
            final String html = IOUtils.toString(inputStream, "UTF-8");
            IOUtils.closeQuietly(inputStream);

            conn.disconnect();

            MARKED_AVAILABLE = StringUtils.contains(html, "<p>Symphony 大法好</p>");

            if (MARKED_AVAILABLE) {
                LOGGER.log(Level.INFO, "[marked] is available, uses it for markdown processing");
            } else {
                LOGGER.log(Level.INFO, "[marked] is not available, uses built-in [flexmark] for markdown processing");
            }
        } catch (final Exception e) {
            LOGGER.log(Level.INFO, "[marked] is not available caused by [" + e.getMessage() + "], uses built-in [flexmark] for markdown processing");
        }
    }

    /**
     * Private constructor.
     */
    private Markdowns() {
    }

    /**
     * Gets the safe HTML content of the specified content.
     *
     * @param content the specified content
     * @param baseURI the specified base URI, the relative path value of href will starts with this URL
     * @return safe HTML content
     */
    public static String clean(final String content, final String baseURI) {
        final Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);

        final String tmp = Jsoup.clean(content, baseURI, Whitelist.relaxed().
                        addAttributes(":all", "id", "target", "class").
                        addTags("span", "hr", "kbd", "samp", "tt", "del", "s", "strike", "u").
                        addAttributes("iframe", "src", "width", "height", "border", "marginwidth", "marginheight").
                        addAttributes("audio", "controls", "src").
                        addAttributes("video", "controls", "src", "width", "height").
                        addAttributes("source", "src", "media", "type").
                        addAttributes("object", "width", "height", "data", "type").
                        addAttributes("param", "name", "value").
                        addAttributes("input", "type", "disabled", "checked").
                        addAttributes("embed", "src", "type", "width", "height", "wmode", "allowNetworking"),
                outputSettings);
        final Document doc = Jsoup.parse(tmp, baseURI, Parser.htmlParser());

        final Elements ps = doc.getElementsByTag("p");
        for (final Element p : ps) {
            p.removeAttr("style");
        }

        final Elements iframes = doc.getElementsByTag("iframe");
        for (final Element iframe : iframes) {
            final String src = StringUtils.deleteWhitespace(iframe.attr("src"));
            if (StringUtils.startsWithIgnoreCase(src, "javascript")
                    || StringUtils.startsWithIgnoreCase(src, "data:")) {
                iframe.remove();
            }
        }

        final Elements objs = doc.getElementsByTag("object");
        for (final Element obj : objs) {
            final String data = StringUtils.deleteWhitespace(obj.attr("data"));
            if (StringUtils.startsWithIgnoreCase(data, "data:")
                    || StringUtils.startsWithIgnoreCase(data, "javascript")) {
                obj.remove();

                continue;
            }

            final String type = StringUtils.deleteWhitespace(obj.attr("type"));
            if (StringUtils.containsIgnoreCase(type, "script")) {
                obj.remove();
            }
        }

        final Elements embeds = doc.getElementsByTag("embed");
        for (final Element embed : embeds) {
            final String data = StringUtils.deleteWhitespace(embed.attr("src"));
            if (StringUtils.startsWithIgnoreCase(data, "data:")
                    || StringUtils.startsWithIgnoreCase(data, "javascript")) {
                embed.remove();

                continue;
            }
        }

        final Elements as = doc.getElementsByTag("a");
        for (final Element a : as) {
            a.attr("rel", "nofollow");

            final String href = a.attr("href");
            if (href.startsWith(Latkes.getServePath())) {
                continue;
            }

            a.attr("target", "_blank");
        }

        final Elements audios = doc.getElementsByTag("audio");
        for (final Element audio : audios) {
            audio.attr("preload", "none");
        }

        final Elements videos = doc.getElementsByTag("video");
        for (final Element video : videos) {
            video.attr("preload", "none");
        }

        String ret = doc.body().html();
        ret = ret.replaceAll("(</?br\\s*/?>\\s*)+", "<br>"); // patch for Jsoup issue

        return ret;
    }

    /**
     * Converts the specified markdown text to HTML.
     *
     * @param markdownText the specified markdown text
     * @return converted HTML, returns an empty string "" if the specified markdown text is "" or {@code null}, returns
     * 'markdownErrorLabel' if exception
     */
    public static String toHTML(final String markdownText) {
        if (Strings.isEmptyOrNull(markdownText)) {
            return "";
        }

        final String cachedHTML = getHTML(markdownText);
        if (null != cachedHTML) {
            return cachedHTML;
        }

        final ExecutorService pool = Executors.newSingleThreadExecutor();
        final long[] threadId = new long[1];

        final Callable<String> call = () -> {
            threadId[0] = Thread.currentThread().getId();

            String html = LANG_PROPS_SERVICE.get("contentRenderFailedLabel");

            if (MARKED_AVAILABLE) {
                html = toHtmlByMarked(markdownText);
                if (!StringUtils.startsWith(html, "<p>")) {
                    html = "<p>" + html + "</p>";
                }
            } else {
                com.vladsch.flexmark.ast.Node document = PARSER.parse(markdownText);
                html = RENDERER.render(document);
                if (!StringUtils.startsWith(html, "<p>")) {
                    html = "<p>" + html + "</p>";
                }

                html = formatMarkdown(html);
            }

            final Document doc = Jsoup.parse(html);
            final List<org.jsoup.nodes.Node> toRemove = new ArrayList<>();
            doc.traverse(new NodeVisitor() {
                @Override
                public void head(final org.jsoup.nodes.Node node, int depth) {
                    if (node instanceof org.jsoup.nodes.TextNode) {
                        final org.jsoup.nodes.TextNode textNode = (org.jsoup.nodes.TextNode) node;
                        final org.jsoup.nodes.Node parent = textNode.parent();

                        if (parent instanceof Element) {
                            final Element parentElem = (Element) parent;

                            if (!parentElem.tagName().equals("code")) {
                                String text = textNode.getWholeText();
                                boolean nextIsBr = false;
                                final org.jsoup.nodes.Node nextSibling = textNode.nextSibling();
                                if (nextSibling instanceof Element) {
                                    nextIsBr = "br".equalsIgnoreCase(((Element) nextSibling).tagName());
                                }

                                if (null != userQueryService) {
                                    try {
                                        final Set<String> userNames = userQueryService.getUserNames(text);
                                        for (final String userName : userNames) {
                                            text = text.replace('@' + userName + (nextIsBr ? "" : " "), "@<a href='" + Latkes.getServePath()
                                                    + "/member/" + userName + "'>" + userName + "</a> ");
                                        }
                                        text = text.replace("@participants ",
                                                "@<a href='https://hacpai.com/article/1458053458339' class='ft-red'>participants</a> ");
                                    } finally {
                                        JdbcRepository.dispose();
                                    }
                                }

                                if (text.contains("@<a href=")) {
                                    final List<org.jsoup.nodes.Node> nodes = Parser.parseFragment(text, parentElem, "");
                                    final int index = textNode.siblingIndex();

                                    parentElem.insertChildren(index, nodes);
                                    toRemove.add(node);
                                } else {
                                    textNode.text(Pangu.spacingText(text));
                                }
                            }
                        }
                    }
                }

                @Override
                public void tail(org.jsoup.nodes.Node node, int depth) {
                }
            });

            toRemove.forEach(node -> node.remove());

            doc.outputSettings().prettyPrint(false);

            String ret = doc.select("body").html();
            ret = StringUtils.trim(ret);

            // cache it
            putHTML(markdownText, ret);

            return ret;
        };

        Stopwatchs.start("Md to HTML");
        try {
            final Future<String> future = pool.submit(call);

            return future.get(MD_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
            LOGGER.log(Level.ERROR, "Markdown timeout [md=" + markdownText + "]");
            Callstacks.printCallstack(Level.ERROR, new String[]{"org.b3log"}, null);

            final Set<Thread> threads = Thread.getAllStackTraces().keySet();
            for (final Thread thread : threads) {
                if (thread.getId() == threadId[0]) {
                    thread.stop();

                    break;
                }
            }
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Markdown failed [md=" + markdownText + "]", e);
        } finally {
            pool.shutdownNow();

            Stopwatchs.end();
        }

        return LANG_PROPS_SERVICE.get("contentRenderFailedLabel");
    }

    private static String toHtmlByMarked(final String markdownText) throws Exception {
        final URL url = new URL(MARKED_ENGINE_URL);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);

        final OutputStream outputStream = conn.getOutputStream();
        IOUtils.write(markdownText, outputStream, "UTF-8");
        IOUtils.closeQuietly(outputStream);

        final InputStream inputStream = conn.getInputStream();
        final String html = IOUtils.toString(inputStream, "UTF-8");
        IOUtils.closeQuietly(inputStream);

        //conn.disconnect();

        return html;
    }

    /**
     * See https://github.com/b3log/symphony/issues/306.
     *
     * @param markdownText
     * @return
     */
    private static String formatMarkdown(final String markdownText) {
        String ret = markdownText;

        final Document doc = Jsoup.parse(markdownText, "", Parser.htmlParser());
        final Elements tagA = doc.select("a");

        for (final Element aTagA : tagA) {
            final String search = aTagA.attr("href");
            final String replace = StringUtils.replace(search, "_", "[downline]");

            ret = StringUtils.replace(ret, search, replace);
        }

        final Elements tagImg = doc.select("img");
        for (final Element aTagImg : tagImg) {
            final String search = aTagImg.attr("src");
            final String replace = StringUtils.replace(search, "_", "[downline]");

            ret = StringUtils.replace(ret, search, replace);
        }

        final Elements tagCode = doc.select("code");
        for (final Element aTagCode : tagCode) {
            final String search = aTagCode.html();
            final String replace = StringUtils.replace(search, "_", "[downline]");

            ret = StringUtils.replace(ret, search, replace);
        }

        String[] rets = ret.split("\n");
        for (final String temp : rets) {
            final String[] toStrong = StringUtils.substringsBetween(temp, "**", "**");
            final String[] toEm = StringUtils.substringsBetween(temp, "_", "_");

            if (toStrong != null && toStrong.length > 0) {
                for (final String strong : toStrong) {
                    final String search = "**" + strong + "**";
                    final String replace = "<strong>" + strong + "</strong>";
                    ret = StringUtils.replace(ret, search, replace);
                }
            }

            if (toEm != null && toEm.length > 0) {
                for (final String em : toEm) {
                    final String search = "_" + em + "_";
                    final String replace = "<em>" + em + "<em>";
                    ret = StringUtils.replace(ret, search, replace);
                }
            }
        }

        ret = StringUtils.replace(ret, "[downline]", "_");

        return ret;
    }

    /**
     * Gets HTML for the specified markdown text.
     *
     * @param markdownText the specified markdown text
     * @return HTML
     */
    public static String getHTML(final String markdownText) {
        final String hash = MD5.hash(markdownText);

        return (String) MD_CACHE.get(hash);
    }

    /**
     * Puts the specified HTML into cache.
     *
     * @param markdownText the specified markdown text
     * @param html         the specified HTML
     */
    private static void putHTML(final String markdownText, final String html) {
        final String hash = MD5.hash(markdownText);

        MD_CACHE.put(hash, html);
    }
}
