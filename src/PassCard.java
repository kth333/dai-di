public class PassCard extends Hand {
    public PassCard(Player player){
        
        //no cards
        super(player,null);
        //since Passcard does not have a type i did not place a type
        //i do not know if i should place a specialType?    
    }
    public boolean isValid(){
        //always valid against any cards
        return true;

    }
    public String getType(){
        return "Pass";
    }
    
}
