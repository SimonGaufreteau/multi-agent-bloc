import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Pilotage {
    Environnement environnement;
    public static int MAX_ITER = 2000;

    public Pilotage(Environnement environnement) {
        this.environnement = environnement;
    }

    public int run(boolean printRes) throws Exception {
        int i = 0;
        while(i<MAX_ITER && !isFinished()){
            Agent agent = environnement.getRandomAgent();
            if(printRes){
                System.out.println("Etape : "+i);
                System.out.println("Agent sélectionné : "+agent.getBloc());
            }
            agent.realiserAction();
            if(printRes){
                System.out.println(environnement);
                System.out.println("\n");
            }
            i++;
        }
        if(i == MAX_ITER) return -1;
        return i;
    }

    public boolean isFinished(){
        ArrayList<Stack<Agent>> stack_list = environnement.getPile_list();
        ArrayList<Bloc> pos_final = environnement.getPosition_finale();

        for(Stack<Agent> stack : stack_list){
            Object[] list = stack.toArray();
            if(list.length==pos_final.size()){
                boolean flag = true;
                for(int i=0;i<pos_final.size();i++){
                    flag = flag && pos_final.get(i).equals(((Agent)list[i]).getBloc());
                }
                if(flag) return true;
            }
        }
        return false;
    }
}
