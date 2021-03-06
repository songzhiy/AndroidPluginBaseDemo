package com.szy.plugininterfacesmodule;

/**
 * Created by songzhiyang on 2019/1/22.
 *
 * @author songzhiyang
 */
public class Constants {

    public final class BroadcastReceiverConstants {
        public static final String DYNAMIC_REGISTER_PLUGIN_BROADCAST_RECIEVER = "dynamic_register_plugin_broadcast_receiver";
        public static final String MANIFEST_REGISTER_PLUGIN_BROADCAST_RECEIVER1 = "manifest_register_plugin_broadcast_receiver1";
        public static final String MANIFEST_REGISTER_PLUGIN_BROADCAST_RECEIVER2 = "manifest_register_plugin_broadcast_receiver2";
    }

    public final class ContentProviderConstants {
        public static final String CONTENT_PROVIDER_PLUGIN_A_SCHEMA = "content_provider_plugin_a_schema";
        public static final String CONTENT_PROVIDER_HOST_APP_SCHEMA = "content_provider_host_app_schema";
    }

    public final class ThatConstants {
        public static final String THAT_INTENT_PLUGIN_NAME = "that_intent_plugin_name";
        public static final String THAT_INTENT_PLUGIN_ACTIVITY_CLASS = "that_intent_plugin_activity_class";

        public static final String THAT_INTENT_ACTIVITY_LAUNCH_MODE = "that_intent_activity_launch_mode";
        public static final String THAT_INTENT_ACTIVITY_LAUNCH_MODE_STANDARD = "that_intent_activity_launch_mode_standard";
        public static final String THAT_INTENT_ACTIVITY_LAUNCH_MODE_SINGLE_TOP = "that_intent_activity_launch_mode_single_top";
        public static final String THAT_INTENT_ACTIVITY_LAUNCH_MODE_SINGLE_INSTANCE = "that_intent_activity_launch_mode_single_instance";
        public static final String THAT_INTENT_ACTIVITY_LAUNCH_MODE_SINGLE_TASK = "that_intent_activity_launch_mode_single_task";

        public static final String THAT_INTENT_PLUGIN_SERVICE_CLASS = "that_intent_plugin_service_class";
        public static final String THAT_INTENT_PLUGIN_SERVICE_REAL_INTENT = "that_intent_plugin_service_real_intent";
    }
}
