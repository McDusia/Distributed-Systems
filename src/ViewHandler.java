import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.MergeView;
import org.jgroups.View;
import java.util.List;

public class ViewHandler extends Thread{

    JChannel channel;
    MergeView view;

    ViewHandler(JChannel ch, MergeView view) {
        this.channel =ch;
        this.view=view;
    }

    public void run() {
        List<View> subgroups= view.getSubgroups();
        View tmp_view=subgroups.get(0);//firstElement(); // picks the first
        Address local_addr= channel.getAddress();//.getLocalAddress();
        if(!tmp_view.getMembers().contains(local_addr)) {
            System.out.println("Not member of the new primary partition ("
                    + tmp_view + "), will re-acquire the state");
            try {
                channel.getState(null, 30000);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            System.out.println("Not member of the new primary partition ("
                    + tmp_view + "), will do nothing");
        }
    }


}
