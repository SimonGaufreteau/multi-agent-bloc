package v2;

import v1.Agent;
import v1.Bloc;

import java.util.*;

public class EnvironnementIntel {
    protected ArrayList<Stack<AgentIntel>> pile_list;
    protected final ArrayList<Bloc> position_finale;
    protected int nb_agents;
    protected ArrayList<AgentIntel> liste_agents;
    protected AgentIntel last_agent;

    public EnvironnementIntel(int nb_list, int nb_agents, boolean stacked) {
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
        liste_agents.add(new AgentIntel(blocs.get(0),getTableBloc(), this,null));
        for(int i=1;i<nb_agents;i++){
            liste_agents.add(new AgentIntel(blocs.get(i),blocs.get(i-1), this,null));
        }

        // Melange des agents
        ArrayList<AgentIntel> agents_shuffle = (ArrayList<AgentIntel>)liste_agents.clone();
        Collections.shuffle(agents_shuffle);

        // Placement des agents
        for(int i=0;i<nb_agents;i++){
            int index = stacked?random.nextInt(nb_list):0;
            Stack<AgentIntel> pile = pile_list.get(index);
            AgentIntel agent = agents_shuffle.get(i);
            pile.push(agent);
        }

    }

    public Stack<AgentIntel> getPileDifferente(AgentIntel agent) {
        Random random = new Random();
        Stack<AgentIntel> pile;
        do{
            pile = pile_list.get(random.nextInt(pile_list.size()));
        }while(pile.contains(agent));
        return pile;
    }

    public AgentIntel getRandomAgent(){
        Random random = new Random();
        return liste_agents.get(random.nextInt(liste_agents.size()));
    }

    public AgentIntel chooseAgent() throws Exception {
        for (AgentIntel agentIntel : liste_agents){
            agentIntel.updatePerception();
        }
        ArrayList<AgentIntel> availableAgents = getAvailableAgents();
        for(AgentIntel agent: availableAgents){
            Object[] agentStackAsList = getAgentStackAsList(agent);

            if(agent.equals(last_agent)) continue;

            // Sinon on regarde si les blocs en dessous sont sur le bon bloc
            boolean isPileOk = true;
            for(Object o : agentStackAsList){
                if(o instanceof AgentIntel currentAgent && !o.equals(agent)){
                    isPileOk = isPileOk && currentAgent.positionOk();
                }
            }

            // Si l'agent n'est pas sur le bon bloc, on va le déplacer
            // Si un des blocs en dessous n'est pas sur sa cible, on déplace le bloc du dessus de pile

            if(!agent.positionOk() || !isPileOk) {
                last_agent = agent;
                return agent;
            }
        }
        //System.out.println(this);
        Random random = new Random();
        return availableAgents.get(random.nextInt(availableAgents.size()));
//        throw new Exception("Erreur lors de la sélection de l'agent. Aucun ne peut être déplacé suivant la stratégie.");
    }

    //Renvoie une liste des agents qui peuvent se déplacer
    public ArrayList<AgentIntel> getAvailableAgents() {
        ArrayList<AgentIntel> availableAgents = new ArrayList<>();
        for(AgentIntel a: liste_agents) {
            try {
                if(getSuperieurActuel(a) == null) {
                    availableAgents.add(a);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return availableAgents;
    }

    public ArrayList<Stack<AgentIntel>> getPile_list() {
        return pile_list;
    }

    public ArrayList<Bloc> getPosition_finale() {
        return position_finale;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Stack<AgentIntel> stack : pile_list){
            int i = 0;
            builder.append("|");
            for(Object o:stack.toArray()){
                if(o instanceof AgentIntel agent){
                    i+=1;
                    builder.append(" ").append(agent.getBloc()).append(" |");
                }
            }
            builder.append("   |".repeat(Math.max(0, nb_agents - i)));
            builder.append("\n");

        }
        return builder.toString();
    }


    public AgentIntel getTableAgent() {
        return null;
    }

    public Bloc getTableBloc(){
        return null;
    }

    public AgentIntel getInferieurActuel(AgentIntel agent) throws Exception {
        Object[] list = getAgentStackAsList(agent);
        for(int i=0;i<list.length;i++){
            if(list[i].equals(agent)){
                return i==0?null:(AgentIntel)list[i-1];
            }
        }
        return null;
    }

    public AgentIntel getSuperieurActuel(AgentIntel agent) throws Exception {
        Object[] list = getAgentStackAsList(agent);
        for(int i=0;i<list.length;i++){
            if(list[i].equals(agent)){
                return (i==list.length-1)?null:(AgentIntel)list[i+1];
            }
        }
        return null;
    }

    public Object[] getAgentStackAsList(AgentIntel agent) throws Exception {
        for(Stack<AgentIntel> stack : pile_list){
            Object[] list = stack.toArray();
            for(Object o : list){
                if(o instanceof AgentIntel && o.equals(agent)){
                    return list;
                }
            }
        }
        throw new Exception("Agent not found in stacks");
    }

    public Stack<AgentIntel> getAgentStack(AgentIntel agent) throws Exception {
        for(Stack<AgentIntel> stack : pile_list){
            Object[] list = stack.toArray();
            for(Object o : list){
                if(o instanceof AgentIntel && o.equals(agent)){
                    return stack;
                }
            }
        }
        throw new Exception("v1.Agent not found in stacks");
    }


    public void pousserSuperieur(AgentIntel agent) throws Exception {
        AgentIntel sup = getSuperieurActuel(agent);
        sup.setEstPousse(true);
    }

    public void deplacerAgent(AgentIntel agent) throws Exception {

        Stack<AgentIntel> stack_actuelle = getAgentStack(agent);

        Stack<AgentIntel> stack_dest = getPileDifferente(agent);

        if(agent.bloc_inferieur_cible==null){
            // On cherche une pile vide pour le 1, si on en trouve une on se déplace dessus
            Stack<AgentIntel> pile_vide = null;
            ArrayList<Stack<AgentIntel>> pile_list = getPile_list();
            for(Stack<AgentIntel> stack : pile_list){
                if(stack.isEmpty()){
                    pile_vide = stack;
                    break;
                }
            }

            if(pile_vide != null)  stack_dest = pile_vide;
        }
        else{
            // On regarde si il y a la cible sur une des piles, si oui on se déplace dessus
            for(AgentIntel currentAgent : getAvailableAgents()) {
                if (!currentAgent.equals(agent)) {
                    if (currentAgent.getBloc().equals(agent.bloc_inferieur_cible)) {
                        stack_dest = getAgentStack(currentAgent);
                        break;
                    }
                }
            }
        }

        stack_actuelle.pop();
        stack_dest.push(agent);


    }
}
