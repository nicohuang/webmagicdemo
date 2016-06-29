package com.flinkinfo.demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashSet;
import java.util.List;
/**
 * page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()")
 * 这段代码使用了XPath，它的意思是“查找所有class属性为'entry-title public'的h1元素，并找到他的strong子节点的a子节点，并提取a节点的文本信息
 */

/**
 * Created by nico on 16/6/27.
 */
public class GithubReboPageProcessor implements PageProcessor
{
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site page = Site.me().setRetryTimes(3).setSleepTime(1000);

    public Site getSite()
    {
        return page;
    }

    public void process(Page page)
    {
        List<String> links = page.getHtml().links().regex(
                "http://posters.imdb.cn/poster/\\d+").all();
        links = removeDuplicate(links);
        // 部分二：定义如何抽取页面信息，并保存下来
        page.addTargetRequests(links);
        page.putField("title", page.getHtml().xpath(
                "//div[@id='imdbleftsecc']/center/h1/text()").toString());
        page.putField("imgurl", page.getHtml().xpath(
                "//div[@id='imdbleftsecc']/center/img/@src").toString());

    }

    public static void main(String[] args)
    {
        for (int i = 1; i <= 3; i++)
        {
            Spider.create(new GithubReboPageProcessor()).addUrl(
                    "http://posters.imdb.cn/poster_page/" + i).thread(5).run();
        }
    }

    public static List removeDuplicate(List list)
    {
        HashSet hs = new HashSet(list);
        list.clear();
        list.addAll(hs);
        return list;
    }
}
