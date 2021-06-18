package com.gmail.pon52_lemon;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class ExplosionListener implements Listener 
{
    private boolean enable_plugin = false;          // プラグインの有効無効
    private ArrayList<String> explosion_player;     // ダメージを受けると爆発するプレイヤーのリスト
    private int explosion_range = 3;                //爆発範囲

    public ExplosionListener()
    {
        this.explosion_player = new ArrayList<>();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if(this.enable_plugin == false)
        {
            return;
        }

        Entity entity = event.getEntity();
        EntityType type = entity.getType();   

        // Entityのタイプがプレイヤーの場合、指定プレイヤー以外は爆発しない 
        if(type == EntityType.PLAYER)
        {
            Player player = (Player)entity;
            if(!this.explosion_player.contains(player.getName()))
            {
                return;
            }
        }

        // LivingEntity のみ爆発
        if(entity instanceof LivingEntity)
        {
            // 爆発処理
            Location loc = entity.getLocation();
            loc.getWorld().createExplosion(entity, this.explosion_range, false);
    
            // // 誘爆を防ぐためにエンティティをKILL
            event.setDamage(event.getDamage() * 1000);
        }
    }

    public void setEnableFlg(Boolean flg)
    {
        this.enable_plugin = flg;
    }

    public Boolean getEnableFlg()
    {
        return this.enable_plugin;
    }

    public void clearExplosionPlayer()
    {
        this.explosion_player.clear();
    }

    public void setExplosionPlayer(String[] players)
    {
        if(this.enable_plugin == false)
        {
            // プラグイン無効時は何もせずリターン
            return;
        }

        for(String player : players)
        {
            // リストにない名前のみ追加
            if(!this.explosion_player.contains(player))
            {
                this.explosion_player.add(player);
            }
        }
    }

    public void removeExplosionPlayer(String[] players)
    {
        if(this.enable_plugin == false)
        {
            // プラグイン無効時は何もせずリターン
            return;
        }

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(players));
        this.explosion_player.removeAll(list);
    }

    public String[] getExplosionPlayer()
    {
        return this.explosion_player.toArray(new String[this.explosion_player.size()]);
    }

    public void setExplosionRange(int range)
    {
        this.explosion_range = range;
    }

    public int getExplosionRange()
    {
        return this.explosion_range;
    }
}
