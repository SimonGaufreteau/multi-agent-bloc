package v2;

import v1.Bloc;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class AgentIntel {
    protected Bloc bloc;
    protected Bloc bloc_inferieur_cible;

    protected AgentIntel agent_inferieur_actuel;
    protected AgentIntel agent_superieur_actuel;

    protected EnvironnementIntel environnement;


    public AgentIntel(Bloc bloc,Bloc inferieur_cible, EnvironnementIntel environnement,Stack<AgentIntel> pile_actuelle) {
        this.bloc = bloc;
        this.bloc_inferieur_cible = inferieur_cible;
        this.agent_inferieur_actuel = null;
        this.agent_superieur_actuel = null;
        this.environnement = environnement;
    }

    public void updatePerception() throws Exception {
        agent_inferieur_actuel = environnement.getInferieurActuel(this);
        agent_superieur_actuel = environnement.getSuperieurActuel(this);
    }

    public void realiserAction() throws Exception {
        updatePerception();
        seDeplacer();
    }

    public void seDeplacer() throws Exception {
        ArrayList<AgentIntel> liste_agents = environnement.getListe_agents();
        ArrayList<AgentIntel> availableAgents = availableAgents();
        // L'agent s'enlève des agents disponibles pour éviter de se déplacer sur lui-même
        availableAgents.remove(this);
        AgentIntel agent_dest = null;

        // On prend un agent aléatoire comme destination par défaut
        if(availableAgents.size() > 1){
            Random random = new Random();
            AgentIntel agent;
            do{
                agent = availableAgents.get(random.nextInt(availableAgents.size()));
            }while(agent.equals(this));
            agent_dest = agent;
        }

        if(bloc_inferieur_cible==null){
            // On cherche une pile vide pour le 1, si on en trouve une on se déplace dessus
            int count = 0;
            for(AgentIntel agentIntel : liste_agents){
                if(agentIntel.agent_inferieur_actuel == null) count++;
            }
            if(count!=environnement.getNbStack())
                agent_dest = null;
        }else {
            // On regarde si il y a la cible sur une des piles, si oui on se déplace dessus
            for(AgentIntel currentAgent : availableAgents) {
                if (currentAgent.getBloc().equals(bloc_inferieur_cible)) {
                    agent_dest = currentAgent;
                    break;
                }
            }
        }
        environnement.deplacerAgent(this,agent_dest);
    }

    public ArrayList<AgentIntel> availableAgents() {
        ArrayList<AgentIntel> availableAgents= new ArrayList<>();
        ArrayList<AgentIntel> liste_agents = environnement.getListe_agents();
        // L'agent demande à tous les agents directement si ils sont disponibles
        for(AgentIntel a: liste_agents) {
            if(a.isAvailable()){
                availableAgents.add(a);
            }
        }
        return availableAgents;
    }

    protected boolean positionOk(){
        if(agent_inferieur_actuel==environnement.getTableAgent()){
            return bloc_inferieur_cible==environnement.getTableBloc();
        }
        return agent_inferieur_actuel.getBloc().equals(bloc_inferieur_cible);
    }

    public Bloc getBloc() {
        return bloc;
    }

    public boolean isAvailable() {
        return agent_superieur_actuel == null;
    }

    public boolean isPileOk() {
        AgentIntel next = agent_inferieur_actuel;
        if(next == null) return positionOk();
        // On demande à chaque agent si la pile est ok via un appel récursif (chaque agent demande à celui
        // en dessous de lui)
        return next.positionOk() && next.isPileOk();
    }

    public boolean tryMove() {
        return isAvailable() && (!positionOk() || !isPileOk());
    }

    @Override
    public String toString() {
        return "AgentIntel{" +
                "bloc=" + bloc ;
    }
}
