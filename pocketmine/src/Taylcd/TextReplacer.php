<?php

namespace Taylcd;

use pocketmine\event\Listener;
use pocketmine\event\server\DataPacketSendEvent;
use pocketmine\network\protocol\TextPacket;
use pocketmine\plugin\PluginBase;

class TextReplacer extends PluginBase implements Listener
{
    public $list = [];

    public function onEnable()
    {
        $this->saveDefaultConfig();
        $this->reloadConfig();

        $this->list = $this->getConfig()->getAll();
        $this->getServer()->getPluginManager()->registerEvents($this, $this);
    }

    public function onDataPacketSend(DataPacketSendEvent $event)
    {
        $pk = $event->getPacket();
        if(!$pk instanceof TextPacket) return;
        $name = $event->getPlayer()->getName();
        foreach($this->list as $item=>$value)
        {
            $item = str_replace('{PLAYER}', $name, $item);
            $value = str_replace('{PLAYER}', $name, $value);

            $pk->message = str_replace($item, $value, $pk->message);
        }
    }
}