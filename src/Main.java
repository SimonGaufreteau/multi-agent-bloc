import java.util.ArrayList;
import java.util.Collections;

public class Main {

    private static boolean PRINT_RES = false;

    public static void main(String[] args) throws Exception {

        //Environnement environnement = new Environnement(3,4,false);
        //Pilotage pilotage = new Pilotage(environnement);
        //System.out.println(environnement);
        int nb_iter = 50;
        long total = 0;
        ArrayList<Integer> arrayList = new ArrayList<>();
        int count_ok = 0;
        int failed = 0;
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
        System.out.println(arrayList);
        System.out.println("Failed : "+failed+" / Ok : "+ count_ok +" (mean : "+total/nb_iter + ")");

    }
}
