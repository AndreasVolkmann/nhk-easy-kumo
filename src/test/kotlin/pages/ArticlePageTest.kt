package pages

import data.Headline
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotStartWith
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import util.loadResource

/**
 * Created by Av on 5/17/2017.
 */
internal class ArticlePageTest {

    val dummyArticlePage = ArticlePage(headline = Headline("x", "x", "x", "x"))

    @Test
    fun `Verify that there are no additional html tags in the content`() {
        val expectedContent = "東京都の多摩動物公園で、２月にチーターの赤ちゃんが同じお母さんから３匹生まれました。雄が２匹で、雌が１匹です。\n" +
                "いまチーターは世界で数が少なくなっていて、多摩動物公園はほかの動物園などと協力してチーターを増やしたいと考えています。３匹を大事に育ててきましたが、重さが３.５ｋｇぐらいになって丈夫になってきたため、４月からみんなに見せています。\n" +
                "３匹は元気に走ったり一緒に遊んだりしています。たくさんの人たちが「かわいい」などと言って見ていました。\n" +
                "多摩動物公園の人は「元気な３匹をぜひ見に来てください」と話していました。"
        val html = this::class.loadResource("Article_k10010959041000.html")
        val body = Jsoup.parse(html).body()
        val content = dummyArticlePage.getContent(body)
        println(content)

        content shouldNotStartWith "<"
        content shouldEqualTo expectedContent
    }

    @Test
    fun verifyHtmlTags() {
        val expectedContent = "製品の安全などを調べているＮＩＴＥによると、去年３月までの５年に、家のこんろや電子レンジなどの事故が９８９件ありました。２３人が火事で亡くなりました。\n" +
                "事故の３０％以上は、油などで汚れたこんろを使ったため火が出たり、近くにあった燃えやすい物に火が移ったりしました。こんろが汚れて火がつきにくいため、何度もつけようとして、出ていたガスが燃えてやけどをした人もいました。\n" +
                "電子レンジも油で汚れたままにしておくと、火が出る危険があります。\n" +
                "ＮＩＴＥは、こんろや電子レンジなどは使ったあとに掃除してほしいと言っています。そして、こんろの近くには燃えやすい物を置かないように言っています。"
        val html = this::class.loadResource("Article_k10010967861000.html")
        val body = Jsoup.parse(html).body()
        val content = dummyArticlePage.getContent(body)
        println(content)

        content shouldNotStartWith "<"
        content shouldEqualTo expectedContent
    }

}