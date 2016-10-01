package com.zlion.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzs on 2016/9/26.
 */
public class GeohashUtil {
    /**
     * The Constant char map BASE_32.
     * */
    private final static char[] BASE_32 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b',
            'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z' };

    /** The Constant DECODE_MAP. */
    private final static Map<Character, Integer> DECODE_MAP = new HashMap<>();
    static
    {
        int counter = 0;
        for (final char c : BASE_32)
        {
            DECODE_MAP.put(c, counter++);
        }
    }

    /** The precision. */
    private static final int precision = 8;

    /** The bits. */
    private static final int[] bits = { 16, 8, 4, 2, 1 };

    /**
     * Decode the given geohash into a latitude and longitude.
     *
     * @param geohash
     *            the geohash
     * @return the double[]
     */
    public static double[] decode(final String geohash)
    {
        if ((geohash == null) || geohash.isEmpty())
        {
            throw new IllegalArgumentException("Argument geohash should not be null.");
        }

        boolean even = true;
        double latitudeError = 90.0;
        double longitudeError = 180.0;
        double latitude;
        double longitude;
        final double[] latitudeInterval = { -90.0, 90.0 };
        final double[] longitudeInterval = { -180.0, 180.0 };
        for (int i = 0; i < geohash.length(); i++)
        {

            final int cd = DECODE_MAP.get(geohash.charAt(i));

            for (int j = 0; j < bits.length; j++)
            {
                final int mask = bits[j];
                if (even)
                {
                    longitudeError /= 2;
                    if ((cd & mask) != 0)
                    {
                        longitudeInterval[0] = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
                    }
                    else
                    {
                        longitudeInterval[1] = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
                    }

                }
                else
                {
                    latitudeError /= 2;

                    if ((cd & mask) != 0)
                    {
                        latitudeInterval[0] = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
                    }
                    else
                    {
                        latitudeInterval[1] = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
                    }
                }

                even = !even;
            }

        }
        latitude = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
        longitude = (longitudeInterval[0] + longitudeInterval[1]) / 2D;

        return new double[] { latitude, longitude, latitudeError, longitudeError };
    }

    /**
     * Encodes the given latitude and longitude into a geohash code.
     *
     * @param latitude
     *            the latitude
     * @param longitude
     *            the longitude
     * @param precision
     *            the precision
     * @return The generated geohash from the given latitude and longitude.
     */
    public static String encode(final double latitude, final double longitude, int precision)
    {
        final StringBuilder geohash = new StringBuilder();
        boolean even = true;
        int bit = 0;
        int ch = 0;

        final double[] latitudeInterval = { -90.0, 90.0 };
        final double[] longitudeInterval = { -180.0, 180.0 };

        while (geohash.length() < precision)
        {
            double mid = 0.0;
            if (even)
            {
                mid = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
                if (longitude > mid)
                {
                    ch |= bits[bit];
                    longitudeInterval[0] = mid;
                }
                else
                {
                    longitudeInterval[1] = mid;
                }

            }
            else
            {
                mid = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
                if (latitude > mid)
                {
                    ch |= bits[bit];
                    latitudeInterval[0] = mid;
                }
                else
                {
                    latitudeInterval[1] = mid;
                }
            }

            even = !even;

            if (bit < 4)
            {
                bit++;
            }
            else
            {
                geohash.append(BASE_32[ch]);
                bit = 0;
                ch = 0;
            }
        }

        return geohash.toString();
    }

    /**
     * Encodes the given latitude and longitude into a geohash code with default precision.
     *
     * @param latitude
     *            the latitude
     * @param longitude
     *            the longitude
     * @return The generated geohash from the given latitude and longitude.
     */
    public static String encode(final double latitude, final double longitude)
    {
        return encode(latitude, longitude, precision);
    }


    /**
     * Gets the precision.
     *
     * @param x
     *            the x
     * @param precision
     *            the precision
     * @return the precision
     */
    private static double getPrecision(final double x, final double precision)
    {
        final double base = Math.pow(10, -precision);
        final double diff = x % base;
        return x - diff;
    }


}
