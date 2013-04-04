package setClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import setServer.User;

public class UserJList extends JList
{
    private ListData listModel;
    public UserJList()
    {
    }
        
    public void createListModel()
    {
        InitGame.debug("User.createListModel");
        listModel = new ListData();
        setModel(listModel);
        InitGame.debug("User.createListModel");
    }
    
//    For now... just using this.  We should figure out the 'correct' way to refresh jlist.
    public void refreshJList()
    {
        listModel = new ListData();
        setModel(listModel);
    }
    
    class ListData extends AbstractListModel
    {
        
        private ArrayList<User> data = Lobby.userArray;
        private Object[] keySet;
        

        private ListData()
        {
            InitGame.debug("UserJList.ListData");
            keySet = data.toArray();
            InitGame.debug(data);
            InitGame.debug("UserJList.ListData");
        }

        @Override
        public int getSize() {
            return keySet.length;
        }

        @Override
        public Object getElementAt(int index) {
            return data.get(index);
        }
        
    }
        
        
}
