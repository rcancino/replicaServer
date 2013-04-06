package com.luxsoft.replica



import org.junit.*
import grails.test.mixin.*

/**
 * EntityConfigurationControllerTests
 * A unit test class is used to test individual methods or blocks of code without considering the surrounding infrastructure
 */
@TestFor(EntityConfigurationController)
@Mock(EntityConfiguration)
class EntityConfigurationControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/entityConfiguration/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.entityConfigurationInstanceList.size() == 0
        assert model.entityConfigurationInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.entityConfigurationInstance != null
    }

    void testSave() {
        controller.save()

        assert model.entityConfigurationInstance != null
        assert view == '/entityConfiguration/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/entityConfiguration/show/1'
        assert controller.flash.message != null
        assert EntityConfiguration.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/entityConfiguration/list'


        populateValidParams(params)
        def entityConfiguration = new EntityConfiguration(params)

        assert entityConfiguration.save() != null

        params.id = entityConfiguration.id

        def model = controller.show()

        assert model.entityConfigurationInstance == entityConfiguration
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/entityConfiguration/list'


        populateValidParams(params)
        def entityConfiguration = new EntityConfiguration(params)

        assert entityConfiguration.save() != null

        params.id = entityConfiguration.id

        def model = controller.edit()

        assert model.entityConfigurationInstance == entityConfiguration
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/entityConfiguration/list'

        response.reset()


        populateValidParams(params)
        def entityConfiguration = new EntityConfiguration(params)

        assert entityConfiguration.save() != null

        // test invalid parameters in update
        params.id = entityConfiguration.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/entityConfiguration/edit"
        assert model.entityConfigurationInstance != null

        entityConfiguration.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/entityConfiguration/show/$entityConfiguration.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        entityConfiguration.clearErrors()

        populateValidParams(params)
        params.id = entityConfiguration.id
        params.version = -1
        controller.update()

        assert view == "/entityConfiguration/edit"
        assert model.entityConfigurationInstance != null
        assert model.entityConfigurationInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/entityConfiguration/list'

        response.reset()

        populateValidParams(params)
        def entityConfiguration = new EntityConfiguration(params)

        assert entityConfiguration.save() != null
        assert EntityConfiguration.count() == 1

        params.id = entityConfiguration.id

        controller.delete()

        assert EntityConfiguration.count() == 0
        assert EntityConfiguration.get(entityConfiguration.id) == null
        assert response.redirectedUrl == '/entityConfiguration/list'
    }
}
