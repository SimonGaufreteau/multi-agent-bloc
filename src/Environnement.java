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

        // Melange des agents
        ArrayList<Agent> agents_shuffle = (ArrayList<Agent>)liste_agents.clone();
        Collections.shuffle(agents_shuffle);

        // Placement des agents
        for(int i=0;i<nb_agents;i++){
            int index = stacked?random.nextInt(nb_list):0;
            Stack<Agent> pile = pile_list.get(index);
            Agent agent = agents_shuffle.get(i);
            pile.push(agent);
        }
    }

    public Stack<Agent> getPileDifferente(Agent agent) {
        Random random = new Random();
        Stack<Agent> pile;
        do{
            pile = pile_list.get(random.nextInt(pile_list.size()));
        }while(pile.contains(agent));
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
            builder.append("   |".repeat(Math.max(0, nb_agents - i)));
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

    public Agent getInferieurActuel(Agent agent) throws Exception {
        Object[] list = getAgentStackAsList(agent);
        for(int i=0;i<list.length;i++){
            if(list[i].equals(agent)){
                return i==0?null:(Agent)list[i-1];
            }
        }
        return null;
    }

    public Agent getSuperieurActuel(Agent agent) throws Exception {
        Object[] list = getAgentStackAsList(agent);
        for(int i=0;i<list.length;i++){
            if(list[i].equals(agent)){
                return (i==list.length-1)?null:(Agent)list[i+1];
            }
        }
        return null;
    }

    public Object[] getAgentStackAsList(Agent agent) throws Exception {
        for(Stack<Agent> stack : pile_list){
            Object[] list = stack.toArray();
            for(Object o : list){
                if(o instanceof Agent && o.equals(agent)){
                    return list;
                }
            }
        }
        throw new Exception("Agent not found in stacks");
    }

    public Stack<Agent> getAgentStack(Agent agent) throws Exception {
        for(Stack<Agent> stack : pile_list){
            Object[] list = stack.toArray();
            for(Object o : list){
                if(o instanceof Agent && o.equals(agent)){
                    return stack;
                }
            }
        }
        throw new Exception("Agent not found in stacks");
    }


    public void pousserSuperieur(Agent agent) throws Exception {
        Agent sup = getSuperieurActuel(agent);
        sup.setEstPousse(true);
    }

    public void deplacerAgent(Agent agent) throws Exception {
        Stack<Agent> stack_actuelle = getAgentStack(agent);
        stack_actuelle.pop();
        Stack<Agent> stack_dest = getPileDifferente(agent);
        stack_dest.push(agent);

    }
}
