package byfr0n.whipslogs.mixin;

import byfr0n.whipslogs.Whipslogs;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {
    @Inject(method = "execute", at = @At("HEAD"))
    private static void onMessageCommand(ServerCommandSource source, Collection<ServerPlayerEntity> targets, SignedMessage message, CallbackInfo ci) {
        if (!source.hasPermissionLevel(2)) {
            String sender = source.getName();
            String recipient = targets.iterator().next().getName().getString();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String logEntry = String.format("[%s] %s -> %s: %s", timestamp, sender, recipient, message.getSignedContent());

            Whipslogs.writeToLog(logEntry);

            String[] ops = source.getServer().getPlayerManager().getOpList().getNames();
            for (String op : ops) {
                ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(op);
                if (player != null) {
                    player.sendMessage(Text.literal(logEntry), false);
                }
            }
        }
    }
}