package com.dimemtl.Model;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;


@DatabaseTable
public class Game implements Model<Integer> {
    @DatabaseField(id = true)
    private Integer id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String description;


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Game(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Game() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(title, game.title) && Objects.equals(description, game.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }
}