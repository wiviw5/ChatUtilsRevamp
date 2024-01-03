package com.github.wiviw5.chatutilsrevamp.events;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class onChatEvent {

    private static final Pattern joinPattern = Pattern.compile(".*(Guild|Friend).*§r§ejoined\\.§r");
    private static final Pattern messagePattern = Pattern.compile("§dFrom §r", Pattern.LITERAL);
    private static final Pattern boopPattern = Pattern.compile("§r§d§lBoop!§r", Pattern.LITERAL);


    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        // The type of the message matters, type 2 is the status which can be safely ignored. Type 1 & Type 0 are both chat.
        if (event.type == 2) {
            return;
        }

        // System.out.println("Raw Formatted messages: <" + event.message.getFormattedText() + ">");
        // System.out.println("Raw Unformatted messages: <" + event.message.getUnformattedText() + ">");
        String formattedText = event.message.getFormattedText();
        String unformattedText = event.message.getUnformattedText();

        // Matcher Compilation if we found anything containing the joined message for guilds or Friends.
        Matcher joinedMatcher = joinPattern.matcher(formattedText);

        // Check for if it was successful, and if so, we move onto customizing the message.
        if (joinedMatcher.find()) {
            event.setCanceled(true);

            // Here we get the username & color, easily editable just in case they switch in the future.
            String username = unformattedText.split(" ")[2];
            char userColor = formattedText.split("§")[3].charAt(0);

            // Compiling the style and message to edit the join message with.
            IChatComponent boopMessage = new ChatComponentText("§d§lBoop! §" + userColor + username);

            ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/boop " + username)).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, boopMessage));

            // Finally sending out the Message to the user and stopping here as we know we don't have to run anything else.
            IChatComponent component = new ChatComponentText(formattedText);
            component.setChatStyle(style);
            Minecraft.getMinecraft().thePlayer.addChatMessage(component);
            return;
        }

        // Matcher Compilation if we found anything containing the "From" message.
        Matcher messageMatcher = messagePattern.matcher(formattedText);

        // Check for if it was successful, and if so, we move onto customizing the message.
        if (messageMatcher.find()){
            // Matcher Compilation and check if we found anything containing the Boop! messages from others
            Matcher boopMatcher = boopPattern.matcher(formattedText);
            boolean boopMatcherBool = boopMatcher.find();
            if (!boopMatcherBool){
                return;
            }
            event.setCanceled(true);

            // Here we remove the final bit from the original message, so we can add a custom one on.
            IChatComponent component = new ChatComponentText(formattedText.split("§r§d§lBoop!§r")[0]);

            // Here we get the user and their rank's color.
            String[] splitMessage = unformattedText.split(" ");
            String user = splitMessage[splitMessage.length-2].replace(":", "");

            IChatComponent boopComponent = new ChatComponentText("§r§d§lBoop!§r");
            char userColor = formattedText.charAt(10);

            IChatComponent boopMessage = new ChatComponentText("§d§lBoop! §" + userColor + user);
            ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/boop " + user)).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, boopMessage));
            boopComponent.setChatStyle(style);

            component.appendSibling(boopComponent);
            Minecraft.getMinecraft().thePlayer.addChatMessage(component);
        }

    }
}