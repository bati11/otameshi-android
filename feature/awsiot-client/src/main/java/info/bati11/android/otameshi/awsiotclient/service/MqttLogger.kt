package info.bati11.android.otameshi.awsiotclient.service

import android.util.Log
import org.eclipse.paho.client.mqttv3.logging.Logger
import java.util.ResourceBundle

class MqttLogger : Logger {

    private enum class Level {
        ERROR,
        WARN,
        INFO,
        DEBUG,
    }

    override fun initialise(
        messageCatalog: ResourceBundle?,
        loggerID: String?,
        resourceName: String?
    ) {
        // NOP
    }

    override fun setResourceName(logContext: String?) {
        // NOP
    }

    override fun isLoggable(level: Int): Boolean {
        return true
    }

    private fun log(
        level: Level,
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        thrown: Throwable?
    ) {
        when (level) {
            Level.ERROR -> Log.e(sourceClass, "sourceMethod:$sourceMethod, msg: $msg, inserts: ${inserts?.joinToString()}", thrown)
            Level.WARN -> Log.w(sourceClass, "sourceMethod:$sourceMethod, msg: $msg, inserts: ${inserts?.joinToString()}", thrown)
            Level.INFO -> Log.i(sourceClass, "sourceMethod:$sourceMethod, msg: $msg, inserts: ${inserts?.joinToString()}", thrown)
            Level.DEBUG -> Log.d(sourceClass, "sourceMethod:$sourceMethod, msg: $msg, inserts: ${inserts?.joinToString()}", thrown)
        }
    }

    override fun severe(sourceClass: String?, sourceMethod: String?, msg: String?) {
        log(Level.ERROR, sourceClass, sourceMethod, msg, null, null)
    }

    override fun severe(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?
    ) {
        log(Level.ERROR, sourceClass, sourceMethod, msg, inserts, null)
    }

    override fun severe(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        thrown: Throwable?
    ) {
        log(Level.ERROR, sourceClass, sourceMethod, msg, inserts, thrown)
    }

    override fun warning(sourceClass: String?, sourceMethod: String?, msg: String?) {
        log(Level.ERROR, sourceClass, sourceMethod, msg, null, null)
    }

    override fun warning(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?
    ) {
        log(Level.WARN, sourceClass, sourceMethod, msg, inserts, null)
    }

    override fun warning(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        thrown: Throwable?
    ) {
        log(Level.WARN, sourceClass, sourceMethod, msg, inserts, thrown)
    }

    override fun info(sourceClass: String?, sourceMethod: String?, msg: String?) {
        log(Level.WARN, sourceClass, sourceMethod, msg, null, null)
    }

    override fun info(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?
    ) {
        log(Level.INFO, sourceClass, sourceMethod, msg, inserts, null)
    }

    override fun info(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        thrown: Throwable?
    ) {
        log(Level.INFO, sourceClass, sourceMethod, msg, inserts, thrown)
    }

    override fun config(sourceClass: String?, sourceMethod: String?, msg: String?) {
        log(Level.INFO, sourceClass, sourceMethod, msg, null, null)
    }

    override fun config(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?
    ) {
        log(Level.INFO, sourceClass, sourceMethod, msg, inserts, null)
    }

    override fun config(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        thrown: Throwable?
    ) {
        log(Level.INFO, sourceClass, sourceMethod, msg, inserts, thrown)
    }

    override fun fine(sourceClass: String?, sourceMethod: String?, msg: String?) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, null, null)
    }

    override fun fine(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?
    ) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, inserts, null)
    }

    override fun fine(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        ex: Throwable?
    ) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, inserts, ex)
    }

    override fun finer(sourceClass: String?, sourceMethod: String?, msg: String?) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, null, null)
    }

    override fun finer(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?
    ) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, inserts, null)
    }

    override fun finer(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        ex: Throwable?
    ) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, inserts, ex)
    }

    override fun finest(sourceClass: String?, sourceMethod: String?, msg: String?) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, null, null)
    }

    override fun finest(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?
    ) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, inserts, null)
    }

    override fun finest(
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        ex: Throwable?
    ) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, inserts, ex)
    }

    override fun log(
        level: Int,
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        thrown: Throwable?
    ) {
        log(Level.INFO, sourceClass, sourceMethod, msg, inserts, thrown)
    }

    override fun trace(
        level: Int,
        sourceClass: String?,
        sourceMethod: String?,
        msg: String?,
        inserts: Array<out Any>?,
        ex: Throwable?
    ) {
        log(Level.DEBUG, sourceClass, sourceMethod, msg, inserts, ex)
    }

    override fun formatMessage(msg: String?, inserts: Array<out Any>?): String {
        return msg ?: ""
    }

    override fun dumpTrace() {
        // NOP
    }
}
