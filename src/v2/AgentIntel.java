package v2;

import v1.Bloc;

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
        environnement.deplacerAgent(this);
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
        return !positionOk() || !isPileOk();
    }

    @Override
    public String toString() {
        return "AgentIntel{" +
                "bloc=" + bloc ;
    }
}
