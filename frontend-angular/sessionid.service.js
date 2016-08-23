(function() {
    'use strict';

    angular
        .module('app')
        .factory('sessionId', sessionId);

    sessionId.$inject = ['$http', 'serverUrl'];

    /* @ngInject */
    function sessionId($http, serverUrl) {
        var service = {
            get: get
        };

        return service;

        function get() {
            return $http.get(serverUrl + '/session/id');
        }
    }
})();
