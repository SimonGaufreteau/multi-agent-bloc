package v2;

import v1.Bloc;

import java.util.Stack;

public class AgentIntel {
    protected Bloc bloc;
    protected Bloc bloc_inferieur_cible;

    protected AgentIntel agent_inferieur_actuel;
    protected AgentIntel agent_superieur_actuel;
    protected boolean est_pousse;

    protected EnvironnementIntel environnement;


    public AgentIntel(Bloc bloc,Bloc inferieur_cible, EnvironnementIntel environnement,Stack<AgentIntel> pile_actuelle) {
        this.bloc = bloc;
        this.bloc_inferieur_cible = inferieur_cible;
        this.agent_inferieur_actuel = null;
        this.agent_superieur_actuel = null;
        this.est_pousse = false;
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

    public void pousser() throws Exception {
        environnement.pousserSuperieur(this);
    }

    public void setEstPousse(boolean b) {
        est_pousse = b;
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

    @Override
    public String toString() {
        return "AgentIntel{" +
                "bloc=" + bloc ;
    }
}
