package v1;

import java.util.Stack;

public class Agent {
    protected Bloc bloc;
    protected Bloc bloc_inferieur_cible;

    protected Agent agent_inferieur_actuel;
    protected Agent agent_superieur_actuel;
    protected boolean est_pousse;

    protected Environnement environnement;


    public Agent(Bloc bloc,Bloc inferieur_cible, Environnement environnement,Stack<Agent> pile_actuelle) {
        this.bloc = bloc;
        this.bloc_inferieur_cible = inferieur_cible;
        this.agent_inferieur_actuel = null;
        this.agent_superieur_actuel = null;
        this.est_pousse = false;
        this.environnement = environnement;
    }

    public void realiserAction() throws Exception {
        agent_inferieur_actuel = environnement.getInferieurActuel(this);
        agent_superieur_actuel = environnement.getSuperieurActuel(this);
        if(est_pousse || !positionOk()){
            if(agent_superieur_actuel == null){
                seDeplacer();
            }
            else pousser();
        }
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
}
