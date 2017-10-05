package com.melvin.mvpframworkdemo.bean;

/**
 * @Author Melvin
 * @CreatedDate 2017/8/15 14:10
 * @Description ${TODO}
 * @Update by       MelvinCen
 * @Date 2017/8/15 14:10
 * @Description ${TODO}
 */

public class WeatherBean {

    /**
     * day_20170815 : {"date":"20170815","temperature":"26℃~32℃","weather":"小雨转阴","weather_id":{"fa":"07","fb":"02"},"week":"星期二","wind":"西风微风"}
     * day_20170816 : {"date":"20170816","temperature":"26℃~32℃","weather":"大雨转中雨","weather_id":{"fa":"09","fb":"08"},"week":"星期三","wind":"西风微风"}
     * day_20170817 : {"date":"20170817","temperature":"26℃~32℃","weather":"中雨转小雨","weather_id":{"fa":"08","fb":"07"},"week":"星期四","wind":"西风微风"}
     * day_20170818 : {"date":"20170818","temperature":"26℃~33℃","weather":"中雨转小雨","weather_id":{"fa":"08","fb":"07"},"week":"星期五","wind":"西风微风"}
     * day_20170819 : {"date":"20170819","temperature":"25℃~32℃","weather":"中雨转阴","weather_id":{"fa":"08","fb":"02"},"week":"星期六","wind":"东风微风"}
     * day_20170820 : {"date":"20170820","temperature":"26℃~33℃","weather":"中雨转小雨","weather_id":{"fa":"08","fb":"07"},"week":"星期日","wind":"西风微风"}
     * day_20170821 : {"date":"20170821","temperature":"26℃~32℃","weather":"中雨转小雨","weather_id":{"fa":"08","fb":"07"},"week":"星期一","wind":"西风微风"}
     */

    private FutureBean future;
    /**
     * humidity : 56%
     * temp : 33
     * time : 15:25
     * wind_direction : 南风
     * wind_strength : 1级
     */

    private SkBean     sk;
    /**
     * city : 上海
     * comfort_index :
     * date_y : 2017年08月15日
     * dressing_advice : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
     * dressing_index : 炎热
     * drying_index :
     * exercise_index : 较不宜
     * temperature : 26℃~32℃
     * travel_index : 较不宜
     * uv_index : 弱
     * wash_index : 不宜
     * weather : 小雨转阴
     * weather_id : {"fa":"07","fb":"02"}
     * week : 星期二
     * wind : 西风微风
     */

    private TodayBean  today;

    public FutureBean getFuture() {
        return future;
    }

    public void setFuture(FutureBean future) {
        this.future = future;
    }

    public SkBean getSk() {
        return sk;
    }

    public void setSk(SkBean sk) {
        this.sk = sk;
    }

    public TodayBean getToday() {
        return today;
    }

    public void setToday(TodayBean today) {
        this.today = today;
    }

    public static class FutureBean {
        /**
         * date : 20170815
         * temperature : 26℃~32℃
         * weather : 小雨转阴
         * weather_id : {"fa":"07","fb":"02"}
         * week : 星期二
         * wind : 西风微风
         */

        private Day20170815Bean day_20170815;
        /**
         * date : 20170816
         * temperature : 26℃~32℃
         * weather : 大雨转中雨
         * weather_id : {"fa":"09","fb":"08"}
         * week : 星期三
         * wind : 西风微风
         */

        private Day20170816Bean day_20170816;
        /**
         * date : 20170817
         * temperature : 26℃~32℃
         * weather : 中雨转小雨
         * weather_id : {"fa":"08","fb":"07"}
         * week : 星期四
         * wind : 西风微风
         */

        private Day20170817Bean day_20170817;
        /**
         * date : 20170818
         * temperature : 26℃~33℃
         * weather : 中雨转小雨
         * weather_id : {"fa":"08","fb":"07"}
         * week : 星期五
         * wind : 西风微风
         */

        private Day20170818Bean day_20170818;
        /**
         * date : 20170819
         * temperature : 25℃~32℃
         * weather : 中雨转阴
         * weather_id : {"fa":"08","fb":"02"}
         * week : 星期六
         * wind : 东风微风
         */

        private Day20170819Bean day_20170819;
        /**
         * date : 20170820
         * temperature : 26℃~33℃
         * weather : 中雨转小雨
         * weather_id : {"fa":"08","fb":"07"}
         * week : 星期日
         * wind : 西风微风
         */

        private Day20170820Bean day_20170820;
        /**
         * date : 20170821
         * temperature : 26℃~32℃
         * weather : 中雨转小雨
         * weather_id : {"fa":"08","fb":"07"}
         * week : 星期一
         * wind : 西风微风
         */

        private Day20170821Bean day_20170821;

        public Day20170815Bean getDay_20170815() {
            return day_20170815;
        }

        public void setDay_20170815(Day20170815Bean day_20170815) {
            this.day_20170815 = day_20170815;
        }

        public Day20170816Bean getDay_20170816() {
            return day_20170816;
        }

        public void setDay_20170816(Day20170816Bean day_20170816) {
            this.day_20170816 = day_20170816;
        }

        public Day20170817Bean getDay_20170817() {
            return day_20170817;
        }

        public void setDay_20170817(Day20170817Bean day_20170817) {
            this.day_20170817 = day_20170817;
        }

        public Day20170818Bean getDay_20170818() {
            return day_20170818;
        }

        public void setDay_20170818(Day20170818Bean day_20170818) {
            this.day_20170818 = day_20170818;
        }

        public Day20170819Bean getDay_20170819() {
            return day_20170819;
        }

        public void setDay_20170819(Day20170819Bean day_20170819) {
            this.day_20170819 = day_20170819;
        }

        public Day20170820Bean getDay_20170820() {
            return day_20170820;
        }

        public void setDay_20170820(Day20170820Bean day_20170820) {
            this.day_20170820 = day_20170820;
        }

        public Day20170821Bean getDay_20170821() {
            return day_20170821;
        }

        public void setDay_20170821(Day20170821Bean day_20170821) {
            this.day_20170821 = day_20170821;
        }

        public static class Day20170815Bean {
            private String date;
            private String temperature;
            private String weather;
            /**
             * fa : 07
             * fb : 02
             */

            private WeatherIdBean weather_id;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBean getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBean weather_id) {
                this.weather_id = weather_id;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public static class WeatherIdBean {
                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }
            }
        }

        public static class Day20170816Bean {
            private String date;
            private String temperature;
            private String weather;
            /**
             * fa : 09
             * fb : 08
             */

            private WeatherIdBean weather_id;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBean getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBean weather_id) {
                this.weather_id = weather_id;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public static class WeatherIdBean {
                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }
            }
        }

        public static class Day20170817Bean {
            private String date;
            private String temperature;
            private String weather;
            /**
             * fa : 08
             * fb : 07
             */

            private WeatherIdBean weather_id;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBean getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBean weather_id) {
                this.weather_id = weather_id;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public static class WeatherIdBean {
                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }
            }
        }

        public static class Day20170818Bean {
            private String date;
            private String temperature;
            private String weather;
            /**
             * fa : 08
             * fb : 07
             */

            private WeatherIdBean weather_id;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBean getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBean weather_id) {
                this.weather_id = weather_id;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public static class WeatherIdBean {
                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }
            }
        }

        public static class Day20170819Bean {
            private String date;
            private String temperature;
            private String weather;
            /**
             * fa : 08
             * fb : 02
             */

            private WeatherIdBean weather_id;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBean getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBean weather_id) {
                this.weather_id = weather_id;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public static class WeatherIdBean {
                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }
            }
        }

        public static class Day20170820Bean {
            private String date;
            private String temperature;
            private String weather;
            /**
             * fa : 08
             * fb : 07
             */

            private WeatherIdBean weather_id;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBean getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBean weather_id) {
                this.weather_id = weather_id;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public static class WeatherIdBean {
                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"date\":\"")
                        .append(date).append('\"');
                sb.append(",\"temperature\":\"")
                        .append(temperature).append('\"');
                sb.append(",\"weather\":\"")
                        .append(weather).append('\"');
                sb.append(",\"weather_id\":")
                        .append(weather_id);
                sb.append(",\"week\":\"")
                        .append(week).append('\"');
                sb.append(",\"wind\":\"")
                        .append(wind).append('\"');
                sb.append('}');
                return sb.toString();
            }
        }

        public static class Day20170821Bean {
            private String date;
            private String temperature;
            private String weather;
            /**
             * fa : 08
             * fb : 07
             */

            private WeatherIdBean weather_id;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WeatherIdBean getWeather_id() {
                return weather_id;
            }

            public void setWeather_id(WeatherIdBean weather_id) {
                this.weather_id = weather_id;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public static class WeatherIdBean {
                private String fa;
                private String fb;

                public String getFa() {
                    return fa;
                }

                public void setFa(String fa) {
                    this.fa = fa;
                }

                public String getFb() {
                    return fb;
                }

                public void setFb(String fb) {
                    this.fb = fb;
                }

                @Override
                public String toString() {
                    final StringBuilder sb = new StringBuilder("{");
                    sb.append("\"fa\":\"")
                            .append(fa).append('\"');
                    sb.append(",\"fb\":\"")
                            .append(fb).append('\"');
                    sb.append('}');
                    return sb.toString();
                }
            }
        }

        @Override
        public String toString() {
            return "FutureBean{" +
                    "day_20170815=" + day_20170815 +
                    ", day_20170816=" + day_20170816 +
                    ", day_20170817=" + day_20170817 +
                    ", day_20170818=" + day_20170818 +
                    ", day_20170819=" + day_20170819 +
                    ", day_20170820=" + day_20170820 +
                    ", day_20170821=" + day_20170821 +
                    '}';
        }
    }

    public static class SkBean {
        private String humidity;
        private String temp;
        private String time;
        private String wind_direction;
        private String wind_strength;

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getWind_direction() {
            return wind_direction;
        }

        public void setWind_direction(String wind_direction) {
            this.wind_direction = wind_direction;
        }

        public String getWind_strength() {
            return wind_strength;
        }

        public void setWind_strength(String wind_strength) {
            this.wind_strength = wind_strength;
        }

        @Override
        public String toString() {
            return "SkBean{" +
                    "humidity='" + humidity + '\'' +
                    ", temp='" + temp + '\'' +
                    ", time='" + time + '\'' +
                    ", wind_direction='" + wind_direction + '\'' +
                    ", wind_strength='" + wind_strength + '\'' +
                    '}';
        }
    }

    public static class TodayBean {
        private String city;
        private String comfort_index;
        private String date_y;
        private String dressing_advice;
        private String dressing_index;
        private String drying_index;
        private String exercise_index;
        private String temperature;
        private String travel_index;
        private String uv_index;
        private String wash_index;
        private String weather;
        /**
         * fa : 07
         * fb : 02
         */

        private WeatherIdBean weather_id;
        private String week;
        private String wind;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getComfort_index() {
            return comfort_index;
        }

        public void setComfort_index(String comfort_index) {
            this.comfort_index = comfort_index;
        }

        public String getDate_y() {
            return date_y;
        }

        public void setDate_y(String date_y) {
            this.date_y = date_y;
        }

        public String getDressing_advice() {
            return dressing_advice;
        }

        public void setDressing_advice(String dressing_advice) {
            this.dressing_advice = dressing_advice;
        }

        public String getDressing_index() {
            return dressing_index;
        }

        public void setDressing_index(String dressing_index) {
            this.dressing_index = dressing_index;
        }

        public String getDrying_index() {
            return drying_index;
        }

        public void setDrying_index(String drying_index) {
            this.drying_index = drying_index;
        }

        public String getExercise_index() {
            return exercise_index;
        }

        public void setExercise_index(String exercise_index) {
            this.exercise_index = exercise_index;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTravel_index() {
            return travel_index;
        }

        public void setTravel_index(String travel_index) {
            this.travel_index = travel_index;
        }

        public String getUv_index() {
            return uv_index;
        }

        public void setUv_index(String uv_index) {
            this.uv_index = uv_index;
        }

        public String getWash_index() {
            return wash_index;
        }

        public void setWash_index(String wash_index) {
            this.wash_index = wash_index;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public WeatherIdBean getWeather_id() {
            return weather_id;
        }

        public void setWeather_id(WeatherIdBean weather_id) {
            this.weather_id = weather_id;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public static class WeatherIdBean {
            private String fa;
            private String fb;

            public String getFa() {
                return fa;
            }

            public void setFa(String fa) {
                this.fa = fa;
            }

            public String getFb() {
                return fb;
            }

            public void setFb(String fb) {
                this.fb = fb;
            }
        }

        @Override
        public String toString() {
            return "TodayBean{" +
                    "city='" + city + '\'' +
                    ", comfort_index='" + comfort_index + '\'' +
                    ", date_y='" + date_y + '\'' +
                    ", dressing_advice='" + dressing_advice + '\'' +
                    ", dressing_index='" + dressing_index + '\'' +
                    ", drying_index='" + drying_index + '\'' +
                    ", exercise_index='" + exercise_index + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", travel_index='" + travel_index + '\'' +
                    ", uv_index='" + uv_index + '\'' +
                    ", wash_index='" + wash_index + '\'' +
                    ", weather='" + weather + '\'' +
                    ", weather_id=" + weather_id +
                    ", week='" + week + '\'' +
                    ", wind='" + wind + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "future=" + future +
                ", sk=" + sk +
                ", today=" + today +
                '}';
    }
}
