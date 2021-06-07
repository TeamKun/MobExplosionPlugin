package com.gmail.pon52_lemon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public class MobExplosion extends JavaPlugin
{

    public boolean enable_plugin = false;       // プラグインの有効無効
    public ArrayList<String> explosion_player;  // ダメージを受けると爆発するプレイヤーのリスト

    @Override
    public void onEnable() 
    {
        // Plugin startup logic
        this.explosion_player = new ArrayList<>();
        getLogger().info("MobExplosionPlugin が導入されました。");
    }

    @Override
    public void onDisable() 
    {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
    {   
        Boolean result = false;

        if (cmd.getName().equals("MobBaku")) 
        {
            if(args[0].equals("on"))
            {
                // プラグイン有効化
                onMobBaku();

                if(args.length > 1)
                {
                    // 爆発するプレイヤー追加
                    String[] list = getPlayerList(args);
                    addPlayer(list);
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
                    addPlayer(list);
                    result = true;
                }
                else
                {
                    sender.sendMessage("プレイヤー名が指定されていません");
                    sender.sendMessage("[MobBaku help] でコマンドを確認してください");
                }
            }
            else if(args[0].equals("help"))
            {
                // ヘルプ取得
                getHelp(sender);
                result = true;
            }
            else
            {
                sender.sendMessage("不正なコマンドです");
                sender.sendMessage("[MobBaku help] でコマンドを確認してください");
            }
        }

        return result;
    }

    private void onMobBaku()
    {
        this.explosion_player.clear();
        this.enable_plugin = true;        
    }

    private void offMobBaku()
    {
        this.enable_plugin = false;        
    }

    private void addPlayer(String[] args)
    {
        if(this.enable_plugin == false)
        {
            // プラグイン無効時は何もせずリターン
            return;
        }

        if(args[0].contains("@"))
        {
            // セレクター指定時の処理

        }
        else
        {
            // プレイヤーID指定時の処理

        }
    }
    
    private void getHelp(CommandSender sender)
    {
        // コマンドのへプルを表示
        sender.sendMessage("MobBaku on <playerId>  [プラグインを有効化 (プレイヤーの指定は任意です)]");
        sender.sendMessage("MobBaku add <playerId>  [ダメージを受けると爆発するプレイヤーを追加]");
        sender.sendMessage("MobBaku off [プラグインを無効化]");
    }

    private String[] getPlayerList(String[] args)
    {
        // コマンドの引数からプレイヤーIDのみを取り出す
        String cmd = args[0];
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(args));
        
        list.remove(cmd);
        String[] result = (String[])list.toArray(new String[list.size()]);

        return result;
    }
}
