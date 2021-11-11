import v1.Environnement;
import v1.Pilotage;
import v2.EnvironnementIntel;
import v2.PilotageIntel;

import java.util.ArrayList;

public class Main {

    private static boolean PRINT_RES = false;
    private static int VERSION = 1;

    public static void main(String[] args) throws Exception {
        int nb_iter = 10000;
        long total = 0;
        int count_ok = 0;
        int failed = 0;
        ArrayList<Integer> arrayList = new ArrayList<>();

        if(VERSION == 1){
            for(int i=0;i<nb_iter;i++){
                Environnement environnement = new Environnement(3,4,false);
                Pilotage pilotage = new Pilotage(environnement);
                int res = pilotage.run(PRINT_RES);
                if(res ==-1){
                    failed++;
                }
                else{
                    count_ok++;
                    arrayList.add(res);
                    total += res;
                }
            }
        }
        else {
            for(int i=0;i<nb_iter;i++){
                EnvironnementIntel environnement = new EnvironnementIntel(3,4,false);
                PilotageIntel pilotage = new PilotageIntel(environnement);
                int res = pilotage.run(PRINT_RES);
                if(res ==-1){
                    failed++;
                }
                else{
                    count_ok++;
                    arrayList.add(res);
                    total += res;
                }
            }
        }

        //System.out.println(arrayList);
        System.out.println("Failed : "+failed+" / Ok : "+ count_ok +" -> "+100*count_ok/(float)nb_iter+"% (mean : "+(float)total/nb_iter + ")");

    }
}
