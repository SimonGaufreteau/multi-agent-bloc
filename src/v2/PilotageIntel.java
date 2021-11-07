package v2;

import v1.Bloc;

import java.util.ArrayList;
import java.util.Stack;

public class PilotageIntel {
    EnvironnementIntel environnement;
    public static int MAX_ITER = 5000;

    public PilotageIntel(EnvironnementIntel environnement) {
        this.environnement = environnement;
    }

    public int run(boolean printRes) throws Exception {
        int i = 0;
        while(i<MAX_ITER && !isFinished()){
            AgentIntel agent = environnement.chooseAgent();
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
        ArrayList<Stack<AgentIntel>> stack_list = environnement.getPile_list();
        ArrayList<Bloc> pos_final = environnement.getPosition_finale();

        for(Stack<AgentIntel> stack : stack_list){
            Object[] list = stack.toArray();
            if(list.length==pos_final.size()){
                boolean flag = true;
                for(int i=0;i<pos_final.size();i++){
                    flag = flag && pos_final.get(i).equals(((AgentIntel)list[i]).getBloc());
                }
                if(flag) return true;
            }
        }
        return false;
    }


}
