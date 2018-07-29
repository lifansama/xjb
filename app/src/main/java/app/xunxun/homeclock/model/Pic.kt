package app.xunxun.homeclock.model

/**
 * Created by fengdianxun on 2017/4/28.
 */

class Pic {

    /**
     * images : [{"startdate":"20170427","fullstartdate":"201704271600","enddate":"20170428","url":"/az/hprichbg/rb/SproutVideo_ZH-CN11890393462_1920x1080.jpg","urlbase":"/az/hprichbg/rb/SproutVideo_ZH-CN11890393462","copyright":"一棵刚萌芽的橡树苗 (© plusphoto/Getty Images)","copyrightlink":"http://www.bing.com/search?q=%E6%A9%A1%E6%A0%91+&form=hpcapt&mkt=zh-cn","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20170427_SproutVideo%22&FORM=HPQUIZ","wp":false,"hsh":"71d1b6424ae34380ed94540a1f4ba4ae","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    var tooltips: TooltipsEntity? = null
    var images: List<ImagesEntity>? = null

    class TooltipsEntity {
        /**
         * loading : 正在加载...
         * previous : 上一个图像
         * next : 下一个图像
         * walle : 此图片不能下载用作壁纸。
         * walls : 下载今日美图。仅限用作桌面壁纸。
         */

        var loading: String? = null
        var previous: String? = null
        var next: String? = null
        var walle: String? = null
        var walls: String? = null
    }

    class ImagesEntity {
        /**
         * startdate : 20170427
         * fullstartdate : 201704271600
         * enddate : 20170428
         * url : /az/hprichbg/rb/SproutVideo_ZH-CN11890393462_1920x1080.jpg
         * urlbase : /az/hprichbg/rb/SproutVideo_ZH-CN11890393462
         * copyright : 一棵刚萌芽的橡树苗 (© plusphoto/Getty Images)
         * copyrightlink : http://www.bing.com/search?q=%E6%A9%A1%E6%A0%91+&form=hpcapt&mkt=zh-cn
         * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20170427_SproutVideo%22&FORM=HPQUIZ
         * wp : false
         * hsh : 71d1b6424ae34380ed94540a1f4ba4ae
         * drk : 1
         * top : 1
         * bot : 1
         * hs : []
         */

        var startdate: String? = null
        var fullstartdate: String? = null
        var enddate: String? = null
        var url: String? = null
        var urlbase: String? = null
        var copyright: String? = null
        var copyrightlink: String? = null
        var quiz: String? = null
        var isWp: Boolean = false
        var hsh: String? = null
        var drk: Int = 0
        var top: Int = 0
        var bot: Int = 0
        var hs: List<*>? = null
    }
}
