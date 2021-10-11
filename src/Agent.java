import java.util.Stack;

public class Agent {
    private Bloc bloc;
    private Bloc bloc_inferieur_cible;

    private Agent agent_inferieur_actuel;
    private Agent agent_superieur_actuel;
    private boolean est_pousse;

    private Environnement environnement;
    private Stack<Agent> pile_actuelle;

    public Agent(Bloc bloc,Bloc inferieur_cible, Environnement environnement,Stack<Agent> pile_actuelle) {
        this.bloc = bloc;
        this.bloc_inferieur_cible = inferieur_cible;
        this.agent_inferieur_actuel = null;
        this.agent_superieur_actuel = null;
        this.est_pousse = false;
        this.environnement = environnement;
        this.pile_actuelle = pile_actuelle;

    }

    public void realiserAction(){
        Stack<Agent> pile_cible = environnement.getPileDifferente(pile_actuelle);
        if(est_pousse){
            if(agent_superieur_actuel == null)
                seDeplacer(pile_cible);
            else
                pousser();
        }
        else if (!positionOk()) {
            if (agent_superieur_actuel == null) {
                seDeplacer(pile_cible);
            } else {
                pousser();
            }
        }
    }

    public void seDeplacer(Stack<Agent> stack){
        if(agent_superieur_actuel==null){
            //System.out.println("Deplacement de "+bloc.getNom());
            pile_actuelle.pop();
            if(agent_inferieur_actuel!=null)
                agent_inferieur_actuel.setSuperieur(null);
            try {
                agent_inferieur_actuel = stack.peek();
                agent_inferieur_actuel.setSuperieur(this);

            }catch (Exception e){
                agent_inferieur_actuel = environnement.getTableAgent();
            }
            stack.push(this);
            pile_actuelle = stack;
        }
    }

    public void pousser(){
        agent_superieur_actuel.setEstPousse(true);
    }

    private void setEstPousse(boolean b) {
        est_pousse = b;
    }

    private boolean positionOk(){
        if(agent_inferieur_actuel==environnement.getTableAgent()){
         return bloc_inferieur_cible==environnement.getTableBloc();
        }
        return agent_inferieur_actuel.getBloc().equals(bloc_inferieur_cible);
    }

    public Bloc getBloc() {
        return bloc;
    }

    public void setSuperieur(Agent agent) {
        this.agent_superieur_actuel = agent;
    }

    public void setInferieur(Agent sup_pile) {
        this.agent_inferieur_actuel = sup_pile;
    }

    public void setPile(Stack<Agent> pile) {
        this.pile_actuelle = pile;
    }
}
