package org.redsxi.config.config

import org.redsxi.config.ConfigKey

class ConfigNotFoundException(key: ConfigKey<*>) : Exception(key.name)