package sg.edu.nus.iss.app.workshop14.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import sg.edu.nus.iss.app.workshop14.model.Contact;
import java.util.List;

@Repository
public class AddressBookRepository {
    
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void save(final Contact ctc){

        redisTemplate.opsForList().leftPush("contactlist", ctc.getId());
        redisTemplate.opsForHash().put("addressbook", ctc.getId(), ctc);

    }

    public Contact findById(String contactId){

        return (Contact) redisTemplate.opsForHash().get("addressbookmap", contactId);

    }

    public List<Contact> findAll(int startIndex){

        List<Object> contacList = redisTemplate.opsForList().range("contactlist",startIndex,10);
        List<Contact> ctcs = redisTemplate.opsForHash().multiGet("addressbookmap", contacList)
            .stream()
            .filter(Contact.class::isInstance)
            .map(Contact.class::cast)
            .toList();

        return null;
    }

}
