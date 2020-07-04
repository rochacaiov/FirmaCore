package br.firmacore.services.tribute.repository;

import br.firmacore.Main;
import br.firmacore.services.tribute.repository.models.Tribute;
import com.dieselpoint.norm.Database;

import java.util.List;

public class TributeRepository {
    private Database database;

    public TributeRepository(Main plugin){
        this.database = plugin.getConnectionFactory().getDatabase();
        if(!plugin.getConnectionFactory().containsTable("tb_tribute")){
            this.database.createTable(Tribute.class);
        }
    }

    public List<Tribute> getAll(){
        return this.database.sql("SELECT * FROM `tb_tribute`").results(Tribute.class);
    }

    public void saveOrUpdate(Tribute tribute) {
        this.database.insert(tribute);
    }

    public void update(Tribute tribute){
        this.database.update(tribute);
    }

    public void delete(Tribute tribute){
        this.database.delete(tribute);
    }

    public Database getDatabase(){
        return this.database;
    }
}
