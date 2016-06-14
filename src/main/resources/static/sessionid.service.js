(function() {
    'use strict';

    angular
        .module('app')
        .factory('sessionId', sessionId);

    sessionId.$inject = ['$http'];

    /* @ngInject */
    function sessionId($http) {
        var service = {
            get: get
        };

        return service;

        function get() {
            return $http.get('/session/id');
        }
    }
})();
