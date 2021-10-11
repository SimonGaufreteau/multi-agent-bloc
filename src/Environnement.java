import java.lang.reflect.Array;
import java.util.*;

public class Environnement {
    private ArrayList<Stack<Agent>> pile_list;
    private final ArrayList<Bloc> position_finale;
    private int nb_agents;
    private ArrayList<Agent> liste_agents;

    public Environnement(int nb_list, int nb_agents, boolean stacked) {
        this.pile_list = new ArrayList<>();
        for(int i=0;i<nb_list;i++) {pile_list.add(new Stack<>());}

        this.nb_agents = nb_agents;

        // Creation des blocs
        ArrayList<Bloc> blocs = new ArrayList<>();
        for(int i=0;i<nb_agents;i++){
            blocs.add(new Bloc(Integer.toString(i+1)));
        }
        this.position_finale = blocs;

        // Creation des agents
        Random random = new Random();
        liste_agents = new ArrayList<>();
        liste_agents.add(new Agent(blocs.get(0),getTableBloc(), this,null));
        for(int i=1;i<nb_agents;i++){
            liste_agents.add(new Agent(blocs.get(i),blocs.get(i-1), this,null));
        }

        ArrayList<Agent> agents_shuffle = (ArrayList<Agent>)liste_agents.clone();
        Collections.shuffle(agents_shuffle);


        for(int i=0;i<nb_agents;i++){
            int index = stacked?random.nextInt(nb_list):0;

            Stack<Agent> pile = pile_list.get(index);
            Agent agent = agents_shuffle.get(i);
            agent.setPile(pile);

            try{
                Agent sup_pile = pile.peek();
                sup_pile.setSuperieur(agent);
                agent.setInferieur(sup_pile);
            }catch (EmptyStackException e){
                agent.setInferieur(getTableAgent());
            }
            pile_list.get(index).push(agent);
        }
    }

    public Stack<Agent> getPileDifferente(Stack<Agent> pile_actuelle) {
        Random random = new Random();
        Stack<Agent> pile;
        do{
            pile = pile_list.get(random.nextInt(pile_list.size()));
        }while(pile.equals(pile_actuelle));
        return pile;
    }

    public Agent getRandomAgent(){
        Random random = new Random();
        return liste_agents.get(random.nextInt(liste_agents.size()));
    }

    public ArrayList<Stack<Agent>> getPile_list() {
        return pile_list;
    }

    public ArrayList<Bloc> getPosition_finale() {
        return position_finale;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Stack<Agent> stack : pile_list){
            int i = 0;
            builder.append("|");
            for(Object o:stack.toArray()){
                if(o instanceof Agent agent){
                    i+=1;
                    builder.append(" ").append(agent.getBloc()).append(" |");
                }
            }
            for(int j = i;j<nb_agents;j++){
                builder.append("   |");
            }
            builder.append("\n");

        }
        return builder.toString();
    }


    public Agent getTableAgent() {
        return null;
    }

    public Bloc getTableBloc(){
        return null;
    }
}
