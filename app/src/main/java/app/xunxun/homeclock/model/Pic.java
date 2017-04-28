package app.xunxun.homeclock.model;

import java.util.List;

/**
 * Created by fengdianxun on 2017/4/28.
 */

public class Pic {

    /**
     * images : [{"startdate":"20170427","fullstartdate":"201704271600","enddate":"20170428","url":"/az/hprichbg/rb/SproutVideo_ZH-CN11890393462_1920x1080.jpg","urlbase":"/az/hprichbg/rb/SproutVideo_ZH-CN11890393462","copyright":"一棵刚萌芽的橡树苗 (© plusphoto/Getty Images)","copyrightlink":"http://www.bing.com/search?q=%E6%A9%A1%E6%A0%91+&form=hpcapt&mkt=zh-cn","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20170427_SproutVideo%22&FORM=HPQUIZ","wp":false,"hsh":"71d1b6424ae34380ed94540a1f4ba4ae","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    private TooltipsEntity tooltips;
    private List<ImagesEntity> images;

    public TooltipsEntity getTooltips() {
        return tooltips;
    }

    public void setTooltips(TooltipsEntity tooltips) {
        this.tooltips = tooltips;
    }

    public List<ImagesEntity> getImages() {
        return images;
    }

    public void setImages(List<ImagesEntity> images) {
        this.images = images;
    }

    public static class TooltipsEntity {
        /**
         * loading : 正在加载...
         * previous : 上一个图像
         * next : 下一个图像
         * walle : 此图片不能下载用作壁纸。
         * walls : 下载今日美图。仅限用作桌面壁纸。
         */

        private String loading;
        private String previous;
        private String next;
        private String walle;
        private String walls;

        public String getLoading() {
            return loading;
        }

        public void setLoading(String loading) {
            this.loading = loading;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getWalle() {
            return walle;
        }

        public void setWalle(String walle) {
            this.walle = walle;
        }

        public String getWalls() {
            return walls;
        }

        public void setWalls(String walls) {
            this.walls = walls;
        }
    }

    public static class ImagesEntity {
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

        private String startdate;
        private String fullstartdate;
        private String enddate;
        private String url;
        private String urlbase;
        private String copyright;
        private String copyrightlink;
        private String quiz;
        private boolean wp;
        private String hsh;
        private int drk;
        private int top;
        private int bot;
        private List<?> hs;

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getFullstartdate() {
            return fullstartdate;
        }

        public void setFullstartdate(String fullstartdate) {
            this.fullstartdate = fullstartdate;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlbase() {
            return urlbase;
        }

        public void setUrlbase(String urlbase) {
            this.urlbase = urlbase;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public String getCopyrightlink() {
            return copyrightlink;
        }

        public void setCopyrightlink(String copyrightlink) {
            this.copyrightlink = copyrightlink;
        }

        public String getQuiz() {
            return quiz;
        }

        public void setQuiz(String quiz) {
            this.quiz = quiz;
        }

        public boolean isWp() {
            return wp;
        }

        public void setWp(boolean wp) {
            this.wp = wp;
        }

        public String getHsh() {
            return hsh;
        }

        public void setHsh(String hsh) {
            this.hsh = hsh;
        }

        public int getDrk() {
            return drk;
        }

        public void setDrk(int drk) {
            this.drk = drk;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getBot() {
            return bot;
        }

        public void setBot(int bot) {
            this.bot = bot;
        }

        public List<?> getHs() {
            return hs;
        }

        public void setHs(List<?> hs) {
            this.hs = hs;
        }
    }
}
