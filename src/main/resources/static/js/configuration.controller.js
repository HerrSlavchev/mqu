'use strict';
 
angular.module('kohonenApp').controller('ConfigurationController', ['HttpService', function(HttpService) {
    var self = this;
    self.configuration={rows:'',cols:'',epochs:'',learningRate:'',radius:''};
    self.res={};
    self.viewNetwork=false;
    
    self.submit = submit;
    self.toggleView = toggleView;
 
    function submit() {
        HttpService.configureNetwork(self.configuration)
        .then(
                function(d) {
                    self.res=d;
                    console.log(d);
                },
                function(errResponse){
                    console.error('Error while setting configuration');
                }
            );
    }
 
    function toggleView() {
        self.viewNetwork = !self.viewNetwork;	
    }
    
}]);