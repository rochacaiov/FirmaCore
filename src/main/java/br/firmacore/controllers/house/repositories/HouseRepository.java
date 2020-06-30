package br.firmacore.controllers.house.repositories;


import br.firmacore.Main;
import br.firmacore.controllers.house.repositories.models.House;
import br.firmacore.controllers.tribute.repositories.models.Tribute;
import com.dieselpoint.norm.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
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
