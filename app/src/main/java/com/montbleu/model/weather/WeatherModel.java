package com.montbleu.model.weather;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ardiya on 4/20/2017.
 */

public class WeatherModel
{
    private String visibility;

    private String timezone;

    private Main main;

    private Clouds clouds;

    private Sys sys;

    private String dt;

    private Coord coord;

    private ArrayList<Weather> weather;

    private String name;

    private String cod;

    private String id;

    private String base;

    private Wind wind;

    public String getVisibility ()
    {
        return visibility;
    }

    public void setVisibility (String visibility)
    {
        this.visibility = visibility;
    }

    public String getTimezone ()
    {
        return timezone;
    }

    public void setTimezone (String timezone)
    {
        this.timezone = timezone;
    }

    public Main getMain ()
    {
        return main;
    }

    public void setMain (Main main)
    {
        this.main = main;
    }

    public Clouds getClouds ()
    {
        return clouds;
    }

    public void setClouds (Clouds clouds)
    {
        this.clouds = clouds;
    }

    public Sys getSys ()
    {
        return sys;
    }

    public void setSys (Sys sys)
    {
        this.sys = sys;
    }

    public String getDt ()
    {
        return dt;
    }

    public void setDt (String dt)
    {
        this.dt = dt;
    }

    public Coord getCoord ()
    {
        return coord;
    }

    public void setCoord (Coord coord)
    {
        this.coord = coord;
    }

    public ArrayList<Weather> getWeather ()
    {
        return weather;
    }

    public void setWeather (ArrayList<Weather> weather)
    {
        this.weather = weather;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getCod ()
    {
        return cod;
    }

    public void setCod (String cod)
    {
        this.cod = cod;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getBase ()
    {
        return base;
    }

    public void setBase (String base)
    {
        this.base = base;
    }

    public Wind getWind ()
    {
        return wind;
    }

    public void setWind (Wind wind)
    {
        this.wind = wind;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [visibility = "+visibility+", timezone = "+timezone+", main = "+main+", clouds = "+clouds+", sys = "+sys+", dt = "+dt+", coord = "+coord+", weather = "+weather+", name = "+name+", cod = "+cod+", id = "+id+", base = "+base+", wind = "+wind+"]";
    }



    public class Sys
    {
        private String country;

        private String sunrise;

        private String sunset;

        private String id;

        private String type;

        public String getCountry ()
        {
            return country;
        }

        public void setCountry (String country)
        {
            this.country = country;
        }

        public String getSunrise ()
        {
            return sunrise;
        }

        public void setSunrise (String sunrise)
        {
            this.sunrise = sunrise;
        }

        public String getSunset ()
        {
            return sunset;
        }

        public void setSunset (String sunset)
        {
            this.sunset = sunset;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [country = "+country+", sunrise = "+sunrise+", sunset = "+sunset+", id = "+id+", type = "+type+"]";
        }
    }


    public class Clouds
    {
        private String all;

        public String getAll ()
        {
            return all;
        }

        public void setAll (String all)
        {
            this.all = all;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [all = "+all+"]";
        }
    }



    public class Wind
    {
        private String deg;

        private String speed;

        public String getDeg ()
        {
            return deg;
        }

        public void setDeg (String deg)
        {
            this.deg = deg;
        }

        public String getSpeed ()
        {
            return speed;
        }

        public void setSpeed (String speed)
        {
            this.speed = speed;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [deg = "+deg+", speed = "+speed+"]";
        }
    }



    public class Main
    {
        private String temp;

        private String temp_min;

        private String humidity;

        private String pressure;

        private String feels_like;

        private String temp_max;

        public String getTemp ()
        {
            return temp;
        }

        public void setTemp (String temp)
        {
            this.temp = temp;
        }

        public String getTemp_min ()
        {
            return temp_min;
        }

        public void setTemp_min (String temp_min)
        {
            this.temp_min = temp_min;
        }

        public String getHumidity ()
        {
            return humidity;
        }

        public void setHumidity (String humidity)
        {
            this.humidity = humidity;
        }

        public String getPressure ()
        {
            return pressure;
        }

        public void setPressure (String pressure)
        {
            this.pressure = pressure;
        }

        public String getFeels_like ()
        {
            return feels_like;
        }

        public void setFeels_like (String feels_like)
        {
            this.feels_like = feels_like;
        }

        public String getTemp_max ()
        {
            return temp_max;
        }

        public void setTemp_max (String temp_max)
        {
            this.temp_max = temp_max;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [temp = "+temp+", temp_min = "+temp_min+", humidity = "+humidity+", pressure = "+pressure+", feels_like = "+feels_like+", temp_max = "+temp_max+"]";
        }
    }


    public class Weather
    {
        private String icon;

        private String description;

        private String main;

        private String id;

        public String getIcon ()
        {
            return icon;
        }

        public void setIcon (String icon)
        {
            this.icon = icon;
        }

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getMain ()
        {
            return main;
        }

        public void setMain (String main)
        {
            this.main = main;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [icon = "+icon+", description = "+description+", main = "+main+", id = "+id+"]";
        }
    }


    public class Coord
    {
        private String lon;

        private String lat;

        public String getLon ()
        {
            return lon;
        }

        public void setLon (String lon)
        {
            this.lon = lon;
        }

        public String getLat ()
        {
            return lat;
        }

        public void setLat (String lat)
        {
            this.lat = lat;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [lon = "+lon+", lat = "+lat+"]";
        }
    }





}
