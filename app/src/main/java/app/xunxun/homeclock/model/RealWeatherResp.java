package app.xunxun.homeclock.model;

import java.util.List;

/**
 * Created by fengdianxun on 2017/2/13.
 */

public class RealWeatherResp {

    /**
     * status : ok
     * lang : zh_CN
     * server_time : 1486997938
     * tzshift : 28800
     * location : [25.1552,121.6544]
     * unit : metric
     * result : {"status":"ok","temperature":14.6,"skycon":"PARTLY_CLOUDY_NIGHT","cloudrate":0.3,"aqi":18,"humidity":0.68,"pm25":11,"precipitation":{"nearest":{"status":"ok","distance":126.09,"intensity":0.1875},"local":{"status":"ok","intensity":0,"datasource":"radar"}},"wind":{"direction":73.99,"speed":25.01}}
     */

    private String status;
    private String lang;
    private int server_time;
    private int tzshift;
    private String unit;
    private ResultEntity result;
    private List<Double> location;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getServer_time() {
        return server_time;
    }

    public void setServer_time(int server_time) {
        this.server_time = server_time;
    }

    public int getTzshift() {
        return tzshift;
    }

    public void setTzshift(int tzshift) {
        this.tzshift = tzshift;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public static class ResultEntity {
        /**
         * status : ok
         * temperature : 14.6
         * skycon : PARTLY_CLOUDY_NIGHT
         * cloudrate : 0.3
         * aqi : 18
         * humidity : 0.68
         * pm25 : 11
         * precipitation : {"nearest":{"status":"ok","distance":126.09,"intensity":0.1875},"local":{"status":"ok","intensity":0,"datasource":"radar"}}
         * wind : {"direction":73.99,"speed":25.01}
         */

        private String status;
        private double temperature;
        private String skycon;
        private double cloudrate;
        private int aqi;
        private double humidity;
        private int pm25;
        private PrecipitationEntity precipitation;
        private WindEntity wind;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public String getSkycon() {
            return skycon;
        }

        public String getSkyconText() {
            if (skycon.equals("CLEAR_DAY")) {
                return "晴天";
            } else if (skycon.equals("CLEAR_NIGHT")) {
                return "晴夜";
            } else if (skycon.equals("PARTLY_CLOUDY_DAY") || skycon.equals("PARTLY_CLOUDY_NIGHT")) {
                return "多云";
            } else if (skycon.equals("CLOUDY")) {
                return "阴";
            } else if (skycon.equals("RAIN")) {
                return "雨";
            } else if (skycon.equals("SNOW")) {
                return "雪";
            } else if (skycon.equals("WIND")) {
                return "风";
            } else if (skycon.equals("FOG")) {
                return "雾";
            } else if (skycon.equals("HAZE")) {
                return "霾";
            } else if (skycon.equals("SLEET")) {
                return "冻雨";
            } else {
                return "";
            }

        }

        public void setSkycon(String skycon) {
            this.skycon = skycon;
        }

        public double getCloudrate() {
            return cloudrate;
        }

        public void setCloudrate(double cloudrate) {
            this.cloudrate = cloudrate;
        }

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public int getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public PrecipitationEntity getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(PrecipitationEntity precipitation) {
            this.precipitation = precipitation;
        }

        public WindEntity getWind() {
            return wind;
        }

        public void setWind(WindEntity wind) {
            this.wind = wind;
        }

        public static class PrecipitationEntity {
            /**
             * nearest : {"status":"ok","distance":126.09,"intensity":0.1875}
             * local : {"status":"ok","intensity":0,"datasource":"radar"}
             */

            private NearestEntity nearest;
            private LocalEntity local;

            public NearestEntity getNearest() {
                return nearest;
            }

            public void setNearest(NearestEntity nearest) {
                this.nearest = nearest;
            }

            public LocalEntity getLocal() {
                return local;
            }

            public void setLocal(LocalEntity local) {
                this.local = local;
            }

            public static class NearestEntity {
                /**
                 * status : ok
                 * distance : 126.09
                 * intensity : 0.1875
                 */

                private String status;
                private double distance;
                private double intensity;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public double getDistance() {
                    return distance;
                }

                public void setDistance(double distance) {
                    this.distance = distance;
                }

                public double getIntensity() {
                    return intensity;
                }

                public void setIntensity(double intensity) {
                    this.intensity = intensity;
                }
            }

            public static class LocalEntity {
                /**
                 * status : ok
                 * intensity : 0.0
                 * datasource : radar
                 */

                private String status;
                private double intensity;
                private String datasource;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public double getIntensity() {
                    return intensity;
                }

                public void setIntensity(double intensity) {
                    this.intensity = intensity;
                }

                public String getDatasource() {
                    return datasource;
                }

                public void setDatasource(String datasource) {
                    this.datasource = datasource;
                }
            }
        }

        public static class WindEntity {
            /**
             * direction : 73.99
             * speed : 25.01
             */

            private double direction;
            private double speed;

            public double getDirection() {
                return direction;
            }

            public void setDirection(double direction) {
                this.direction = direction;
            }

            public double getSpeed() {
                return speed;
            }

            public void setSpeed(double speed) {
                this.speed = speed;
            }
        }
    }
}
