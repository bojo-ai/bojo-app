package ai.bojo.app.util

import spock.lang.Specification

class SlugIdSpec extends Specification {

    def 'should create a new slug identifier'() {
        when:
        def slugId = SlugId.create()

        then:
        slugId.matches('^[a-zA-Z0-9_-]{22}$')
    }

    def 'should create a new slug identifier from a given UUID'() {
        given:
        UUID uuid = UUID.fromString('be0243d5-6da7-4b07-ad21-51515f1abeb9')

        when:
        def slugId = SlugId.create(uuid)

        then:
        slugId.matches('^[a-zA-Z0-9_-]{22}$')
    }

    def 'should convert a slugId into an UUID value'() {
        given:
        def slugId = 'vgJD1W2nSwetIVFRXxq-uQ'

        when:
        def uuid = SlugId.toUUID(slugId)

        then:
        uuid == UUID.fromString('ad215151-5f1a-beb9-be02-43d56da74b07')
    }
}
