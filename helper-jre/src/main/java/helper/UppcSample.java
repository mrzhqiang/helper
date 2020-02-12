package helper;
import static helper.UppcHelper.*;

public class UppcSample {
    public static void main(String[] args) {
        Mensuration mo = new Mensuration();
        Mensuration mb = new Mensuration();
        Mensuration mi = new Mensuration();
        mo.r = 7.83702;
        mo.theta = 48.1;
        mo.alpha = 14.98;
        mb.r = 9.93802;
        mb.theta = 170.8;
        mb.alpha = 11.56;
        mi.r = 5.45302;
        mi.theta = 0.4;
        mi.alpha = 21.6;

        Coordinate coord = transMeasureXYZ(mo, mb, mi);
        System.out.println(coord.x);
        System.out.println(coord.y);
        System.out.println(coord.z);
        System.out.println();

        WorldMap mapo = new WorldMap();
        WorldMap mapb = new WorldMap();
        WorldMap mapc = new WorldMap();

        mapo.x = 13440333.439102653;
        mapb.x = 13440317.800862981;
        mapc.x = 13440333.439102653;

        mapo.y = 3667293.9329119506;
        mapb.y = 3667294.0635416377;
        mapc.y = 3667283.5805092556;

        LatLon latlono = new LatLon();
        LatLon latlonb = new LatLon();
        LatLon latlonc = new LatLon();
        latlono.lat = 31.265625010488165;
        latlonb.lat = 31.265624867196134;
        latlonc.lat = 31.265545160960247;
        latlono.lon = 120.73656958295032;
        latlonb.lon = 120.73642912320791;
        latlonc.lon = 120.73656952008605;
        double Lb = 15.8;
        double Lc = 10.4;
        WorldMap worldMap = transMeasureMap(coord, mapo, mapb, mapc, Lb, Lc);
        System.out.println(doubleFormat9(worldMap.x));
        System.out.println(worldMap.y);
        System.out.println();

        LatLon latLon = transMeasureWJ(coord, latlono, latlonb, latlonc, Lb, Lc);
        System.out.println(latLon.lat);
        System.out.println(latLon.lon);
    }
}
