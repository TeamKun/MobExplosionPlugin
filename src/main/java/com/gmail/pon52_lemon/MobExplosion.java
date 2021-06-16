package com.gmail.pon52_lemon;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class MobExplosion extends JavaPlugin
{
    private ExplosionListener obj_explosion;

    @Override
    public void onEnable() 
    {
        this.obj_explosion = new ExplosionListener();
        getServer().getPluginManager().registerEvents(this.obj_explosion, this);

        getLogger().info("MobExplosionPlugin が導入されました。");
    }

    @Override
    public void onDisable() 
    {
        // Plugin shutdown logic
    }

    // タブ補完
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) 
    {
        switch(args.length)
        {
            case 1:
                return Stream.of("on", "off", "add", "help", "info")
                        .filter(e -> e.startsWith(args[0]))
                        .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    // コマンド処理
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
    {   
        Boolean result = false;

        if(args.length == 0)
        {
            sender.sendMessage("不正なコマンドです");
            sender.sendMessage("[mobbaku help] でコマンドを確認してください");
        }
        else if (cmd.getName().equalsIgnoreCase("mobbaku")) 
        {
            if(args[0].equals("on"))
            {
                // プラグイン有効化
                onMobBaku();

                if(args.length > 1)
                {
                    // 爆発するプレイヤー追加
                    String[] list = getPlayerList(args);
                    addPlayer(sender, list);
                }

                result = true;
            }
            else if(args[0].equals("off"))
            {
                // プラグイン無効化
                offMobBaku();
                result = true;
            }
            else if(args[0].equals("add"))
            {
                if(args.length > 1)
                {
                // 爆発するプレイヤー追加
                    String[] list = getPlayerList(args);
                    addPlayer(sender, list);
                    result = true;
                }
                else
                {
                    sender.sendMessage("プレイヤー名が指定されていません");
                    sender.sendMessage("[mobbaku help] でコマンドを確認してください");
                }
            }
            else if(args[0].equals("help"))
            {
                // ヘルプ取得
                getHelp(sender);
                result = true;
            }
            else if(args[0].equals("info"))
            {
                getInfo();
                result = true;
            }
            else
            {
                sender.sendMessage("不正なコマンドです");
                sender.sendMessage("[mobbaku help] でコマンドを確認してください");
            }
        }

        return result;
    }

    // 起動コマンド処理
    private void onMobBaku()
    {
        this.obj_explosion.clearExplosionPlayer();
        this.obj_explosion.setEnableFlg(true);
    }

    // 終了コマンド処理
    private void offMobBaku()
    {
        this.obj_explosion.setEnableFlg(false);
    }

    // プレイヤー追加コマンド処理
    private void addPlayer(CommandSender sender, String[] args)
    {
        if(args[0].equals("@a") || args[0].equals("@r"))
        {
            // セレクター指定時の処理
            List<Entity> entityList = Bukkit.selectEntities(sender, args[0]);
            
            if(!entityList.isEmpty())
            {
                List<String> pl_list = new ArrayList<>();
                for(Entity entity : entityList)
                {
                    Player pl = (Player)entity;
                    pl_list.add(pl.getName());
                }

                this.obj_explosion.setExplosionPlayer(pl_list.toArray(new String[pl_list.size()]));
            }
        }
        else
        {
            // プレイヤーID指定時の処理
            this.obj_explosion.setExplosionPlayer(args);
        }
    }
    
    // コマンドヘルプ取得処理
    private void getHelp(CommandSender sender)
    {
        // コマンドのへプルを表示
        sender.sendMessage("mobbaku on <player>  [プラグインを有効化 (プレイヤーの指定は任意です)]");
        sender.sendMessage("mobbaku add <player>  [ダメージを受けると爆発するプレイヤーを追加]");
        sender.sendMessage("mobbaku off [プラグインを無効化]");
    }

    // コマンド引数からプレイヤー名のみ抽出
    private String[] getPlayerList(String[] args)
    {
        // コマンドの引数からプレイヤーIDのみを取り出す
        String cmd = args[0];
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(args));
        
        list.remove(cmd);
        String[] result = (String[])list.toArray(new String[list.size()]);

        return result;
    }

    // 試験用
    private void getInfo()
    {
        getLogger().info("Enable Plugin : " + this.obj_explosion.getEnableFlg());
        getLogger().info("Explosion Player : " + Arrays.toString(this.obj_explosion.getExplosionPlayer()));
        getLogger().info("Explosion Range : " + this.obj_explosion.getExplosionRange());
    }
}
