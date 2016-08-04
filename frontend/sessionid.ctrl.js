(function() {
    'use strict';

    angular
        .module('app')
        .controller('SessionCtrl', SessionCtrl);

    SessionCtrl.$inject = ['$http','sessionId'];

    /* @ngInject */
    function SessionCtrl($http, sessionId) {

        var vm = this;
        vm.id="";
        activate();

        function activate() {
            sessionId.get().then(function(response){
                vm.id = response.data.id;
            });
        }
    }
})();
