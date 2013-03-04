package setClient;

import java.util.LinkedHashMap;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;

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
        
        private LinkedHashMap<String, User> data = User.userList;
        private Object[] keySet;
        

        private ListData()
        {
            InitGame.debug("UserJList.ListData");
            InitGame.debug(this.data);
            keySet = User.userList.keySet().toArray();
            InitGame.debug("UserJList.ListData");
        }

        @Override
        public int getSize() {
            return keySet.length;
        }

        @Override
        public Object getElementAt(int index) {
            return User.userList.get(keySet[index].toString());
        }
        
    }
        
        
}
