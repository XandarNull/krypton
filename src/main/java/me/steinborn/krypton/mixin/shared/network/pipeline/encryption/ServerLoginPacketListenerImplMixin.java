package me.steinborn.krypton.mixin.shared.network.pipeline.encryption;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.steinborn.krypton.mod.shared.network.ClientConnectionEncryptionExtension;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

@Mixin(ServerLoginPacketListenerImpl.class)
public class ServerLoginPacketListenerImplMixin {
    @Shadow @Final Connection connection;

    @WrapOperation(method = "handleKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;setEncryptionKey(Ljavax/crypto/Cipher;Ljavax/crypto/Cipher;)V"))
    private void onKey$setupEncryption(Connection instance, Cipher decryptCipher, Cipher encryptCipher, Operation<Void> original, @Local SecretKey secretKey) throws GeneralSecurityException {
        if (secretKey != null) {
            ((ClientConnectionEncryptionExtension) instance).setupEncryption(secretKey);
        }
        original.call(instance, decryptCipher, encryptCipher);
    }
}
