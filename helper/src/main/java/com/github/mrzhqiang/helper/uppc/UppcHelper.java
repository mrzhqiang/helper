package com.github.mrzhqiang.helper.uppc;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.text.DecimalFormat;

public class UppcHelper {
    public static class Mensuration {
        public double r;
        public double alpha;
        public double theta;
    }

    public static class Coordinate {
        public double x;
        public double y;
        public double z;
    }

    public static class WorldMap {
        public double x;
        public double y;
    }

    public static class LatLon {
        public double lat;
        public double lon;
    }

    /**
     * 函数将卫星测量值转换为XYZ坐标
     * 输入参数mo、mb、mi分别是参考点O、B、i号卫星的三维测量值
     * 输出参数为i号卫星XYZ坐标
     */
    public static Coordinate transMeasureXYZ(Mensuration mo, Mensuration mb, Mensuration mi) {
        double r1 = mo.r;
        double theta1 = mo.theta / 180 * PI;
        double alpha1 = mo.alpha / 180 * PI;
        double r2 = mb.r;
        double theta2 = mb.theta / 180 * PI;
        double alpha2 = mb.alpha / 180 * PI;
        double r3 = mi.r;
        double theta3 = mi.theta / 180 * PI;
        double alpha3 = mi.alpha / 180 * PI;

        Coordinate coord = new Coordinate();
        /**判断测量值是否为原点*/
        if (abs(sqrt(pow((cos(alpha1) * r1), 2) + pow(cos(alpha3) * r3, 2) - 2 * cos(theta1 - theta3) * cos(alpha1) * r1 * cos(alpha3) * r3)) <= pow(10, -6)) {
            coord.x = 0;
            coord.y = 0;
            coord.z = 0;
            return coord;
        }

        /**计算B点在X轴上的坐标*/
        double XB = sqrt(pow((cos(alpha1) * r1), 2) + pow((cos(alpha2) * r2), 2) - 2 * cos(theta1 - theta2) * cos(alpha1) * r1 * cos(alpha2) * r2);

        /**计算角A'OB的值*/
        double angle_AOB = acos((pow(XB, 2) + pow((cos(alpha1) * r1), 2) - pow((cos(alpha2) * r2), 2)) / (2 * cos(alpha1) * r1 * XB));

        /**计算距离Oi'*/
        double L_oi = sqrt(pow((cos(alpha1) * r1), 2) + pow((cos(alpha3) * r3), 2) - 2 * cos(theta1 - theta3) * cos(alpha1) * r1 * cos(alpha3) * r3);

        /**计算角A'Oi'的值*/
        double D_theta = theta3 - theta1;

        /**公式计算*/
        double angle_AOI = 0;
        if (((theta2 - theta1) >= 0 && (theta2 - theta1) < PI) || ((theta2 - theta1) >= -2 * PI && (theta2 - theta1) < -PI)) {
            /**测量结构A在一、二象限*/
            if ((D_theta >= 0 && D_theta < PI) || (D_theta >= -2 * PI && D_theta < -PI)) {
                /**Oi在OA逆时针180~360内*/
                angle_AOI = -acos((pow(L_oi, 2) + pow((cos(alpha1) * r1), 2) - pow((cos(alpha3) * r3), 2)) / (2 * cos(alpha1) * r1 * L_oi));
            } else if ((D_theta >= PI && D_theta < 2 * PI) || (D_theta >= -PI && D_theta < 0)) {
                /**Oi在OA逆时针0~180内*/
                angle_AOI = acos((pow(L_oi, 2) + pow((cos(alpha1) * r1), 2) - pow((cos(alpha3) * r3), 2)) / (2 * cos(alpha1) * r1 * L_oi));
            } else {
                coord.x = 0;
                coord.y = 0;
                coord.z = 0;
                return coord;
            }
            /**计算Xi、Yi、Zi*/
            coord.x = L_oi * cos(angle_AOI + angle_AOB);
            coord.y = L_oi * sin(angle_AOI + angle_AOB);
            coord.z = sin(alpha3) * r3 - sin(alpha1) * r1;
            return coord;
        } else if (((theta2 - theta1) >= PI & (theta2 - theta1) < 2 * PI) || ((theta2 - theta1) >= -PI & (theta2 - theta1) < 0)) {
            /**测量结构A在三、四象限*/
            if ((D_theta >= 0 & D_theta < PI) || (D_theta >= -2 * PI & D_theta < -PI)) {
                /**Oi在OA逆时针180~360内*/
                angle_AOI = acos((pow(L_oi, 2) + pow((cos(alpha1) * r1), 2) - pow((cos(alpha3) * r3), 2)) / (2 * cos(alpha1) * r1 * L_oi));
            } else if ((D_theta >= PI & D_theta < 2 * PI) || (D_theta >= -PI & D_theta < 0)) {
                /**Oi在OA逆时针0~180内*/
                angle_AOI = -acos((pow(L_oi, 2) + pow((cos(alpha1) * r1), 2) - pow((cos(alpha3) * r3), 2)) / (2 * cos(alpha1) * r1 * L_oi));
            } else {
                coord.x = 0;
                coord.y = 0;
                coord.z = 0;
                return coord;
            }
            /**计算Xi、Yi、Zi*/
            coord.x = L_oi * cos(angle_AOI + angle_AOB);
            coord.y = -L_oi * sin(angle_AOI + angle_AOB);
            coord.z = sin(alpha3) * r3 - sin(alpha1) * r1;
            return coord;
        } else {
            coord.x = 0;
            coord.y = 0;
            coord.z = 0;
            return coord;
        }
    }

    /**
     * 根据测量值coord，求其世界坐标
     *
     * @param coord 测量值
     * @param mapO  参考点O
     * @param mapB  参考点B
     * @param mapC  参考点C
     * @param Lb    OB距离
     * @param Lc    OC距离
     * @return
     */
    public static WorldMap transMeasureMap(Coordinate coord, WorldMap mapO, WorldMap mapB, WorldMap mapC, double Lb, double Lc) {
        WorldMap worldMap = new WorldMap();
        worldMap.x = mapO.x + coord.x * (mapB.x - mapO.x) / Lb + coord.y * (mapC.x - mapO.x) / Lc;
        worldMap.y = mapO.y + coord.x * (mapB.y - mapO.y) / Lb + coord.y * (mapC.y - mapO.y) / Lc;
        return worldMap;
    }

    /**
     * 根据测量值coord，求其经纬度
     *
     * @param coord
     * @param latLonO
     * @param latLonB
     * @param latLonC
     * @param Lb
     * @param Lc
     * @return
     */
    public static LatLon transMeasureWJ(Coordinate coord, LatLon latLonO, LatLon latLonB, LatLon latLonC, double Lb, double Lc) {
        LatLon latlon = new LatLon();
        latlon.lat = latLonO.lat + coord.x * (latLonB.lat - latLonO.lat) / Lb + coord.y * (latLonC.lat - latLonO.lat) / Lc;
        latlon.lon = latLonO.lon + coord.x * (latLonB.lon - latLonO.lon) / Lb + coord.y * (latLonC.lon - latLonO.lon) / Lc;
        return latlon;
    }

    public static String doubleFormat9(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("###0.000000000");//格式化设置
        return decimalFormat.format(d);
    }
}
