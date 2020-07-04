package br.firmacore.services.house.repository;


import br.firmacore.Main;
import br.firmacore.services.house.repository.model.House;
import com.dieselpoint.norm.Database;

import java.util.List;

public class HouseRepository {
    private Database database;

    public HouseRepository(Main plugin) {
        this.database = plugin.getConnectionFactory().getDatabase();
        if(!plugin.getConnectionFactory().containsTable("tb_house")){
            this.database.createTable(House.class);
        }
    }

    public List<House> getAll(){
        return this.database.sql("SELECT * FROM `tb_house`").results(House.class);
    }

    public void saveOrUpdate(House house) {
        this.database.insert(house);
    }

    public void update(House house){
        this.database.update(house);
    }

    public void delete(House house){
        this.database.delete(house);
    }

}
